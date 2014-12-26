apt-get install -y unzip wget git

# install Java

# wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_java7_64.sh && bash equip_java7_64.sh

# Install play
cd /opt
wget http://downloads.typesafe.com/play/1.2.7/play-1.2.7.zip
unzip play-1.2.7.zip
chmod +x /opt/play-1.2.7/play
chmod -R a+rw /opt/play-1.2.7/*

echo 'PATH=${PATH}:/opt/play-1.2.7' >> /etc/profile

# install MySQL
# debconf-set-selections <<< 'mysql-server mysql-server/root_password password ilikerandompasswords'
# debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password ilikerandompasswords'
# apt-get install -y mysql-server

# Install jdbc mysql driver
apt-get install -y libmysql-java

# Create database
mysql -u root -pilikerandompasswords -Bse "CREATE DATABASE applegame;"
mysql -u root -pilikerandompasswords -Bse "GRANT ALL ON applegame.* to root identified by 'ilikerandompasswords';"
# Import db dump

cd /vagrant
mysql -u root -pilikerandompasswords applegame < provision/data/applegame1611.sql
# drop
# mysqladmin -u root -pilikerandompasswords drop applegame
# mysql -u root -pilikerandompasswords -e 'drop database applegame;'

play dependencies