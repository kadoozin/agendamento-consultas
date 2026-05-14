Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Import-DotEnv {
  param(
    [Parameter(Mandatory = $true)]
    [string]$Path
  )

  if (-not (Test-Path -LiteralPath $Path)) {
    throw ".env nao encontrado em $Path"
  }

  Get-Content -LiteralPath $Path | ForEach-Object {
    $line = $_.Trim()
    if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith("#")) {
      return
    }

    $parts = $line -split "=", 2
    if ($parts.Count -ne 2) {
      return
    }

    $key = $parts[0].Trim()
    $value = $parts[1].Trim()

    if ($value.Length -ge 2) {
      if (($value.StartsWith('"') -and $value.EndsWith('"')) -or ($value.StartsWith("'") -and $value.EndsWith("'"))) {
        $value = $value.Substring(1, $value.Length - 2)
      }
    }

    [Environment]::SetEnvironmentVariable($key, $value, "Process")
  }
}

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
Set-Location $repoRoot

Import-DotEnv -Path (Join-Path $repoRoot ".env")

Write-Host "[1/3] Subindo Postgres..."
docker compose up -d postgres | Out-Host

$containerId = (docker compose ps -q postgres).Trim()
if (-not $containerId) {
  throw "Nao foi possivel obter o container do postgres."
}

Write-Host "[2/3] Aguardando banco ficar pronto..."
$deadline = (Get-Date).AddMinutes(2)
do {
  $health = (docker inspect --format "{{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}" $containerId).Trim()
  if ($health -eq "healthy") {
    break
  }
  if ($health -eq "exited") {
    docker logs --tail 100 $containerId | Out-Host
    throw "Container do Postgres encerrou durante inicializacao."
  }
  Start-Sleep -Seconds 2
} while ((Get-Date) -lt $deadline)

if ($health -ne "healthy") {
  throw "Timeout aguardando Postgres ficar healthy (status atual: $health)."
}

Write-Host "[3/3] Subindo API Spring Boot..."
cmd /c mvnw.cmd spring-boot:run
