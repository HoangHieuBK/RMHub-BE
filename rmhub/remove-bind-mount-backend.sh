#!/bin/sh
echo "=========================================================================="
echo "Removing bind mount back-end..."
sudo rm -rf /rmhub/mivisu-connector-api/
sudo rm -rf /rmhub/mivisu-connector-ssilm/
sudo rm -rf /rmhub/device-mgmt-module/
sudo rm -rf /rmhub/traffic-logger-module/
sudo rm -rf /rmhub/weather-station-module/
sudo rm -rf /rmhub/notification-module/

echo "=========================================================================="
echo "Done!"
