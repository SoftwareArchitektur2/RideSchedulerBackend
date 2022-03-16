Write-Host "Starting Ride-Scheduler..."

docker compose down  1> tmp.txt
Remove-Item tmp.txt

Write-Host "Still starting Ride-Scheduler..."
docker compose build --quiet
Write-Host "Still starting Ride-Scheduler..."
docker compose up -d 1> tmp.txt
Remove-Item tmp.txt

Write-Host ""
Write-Host "Ride-Scheduler has been started!"
Write-Host ""

Write-Host "______ _     _        _____      _              _       _           "
Write-Host "| ___ (_)   | |      /  ___|    | |            | |     | |          "
Write-Host "| |_/ /_  __| | ___  \ ``--.  ___| |__   ___  __| |_   _| | ___ _ __ "
Write-Host "|    /| |/ _`` |/ _ \  ``--. \/ __| '_ \ / _ \/ _`` | | | | |/ _ \ '__|"
Write-Host "| |\ \| | (_| |  __/ /\__/ / (__| | | |  __/ (_| | |_| | |  __/ |   "
Write-Host "\_| \_|_|\__,_|\___| \____/ \___|_| |_|\___|\__,_|\__,_|_|\___|_|   "
