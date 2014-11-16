apt-get install -y unzip wget git

# Install play
cd /opt
wget http://downloads.typesafe.com/play/1.2.7/play-1.2.7.zip
unzip play-1.2.7.zip
chmod +x /opt/play-1.2.7/play
chmod -R a+rw /opt/play-1.2.7/*

echo 'PATH=${PATH}:/opt/play-1.2.7' >> /etc/profile

# Install jdbc mysql driver
apt-get install -y libmysql-java

# Create database
mysql -u root -pilikerandompasswords -Bse "CREATE DATABASE applegame;"
mysql -u root -pilikerandompasswords -Bse "GRANT ALL ON applegame.* to root identified by 'ilikerandompasswords';"
# Import db dump
mysql -u root -pilikerandompasswords applegame < data/dump.sql
