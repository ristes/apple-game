# -*- mode: ruby -*-
# vi: set ft=ruby :

project_name = "applegame"

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "ubuntu/trusty64"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  config.vm.network "forwarded_port", guest: 9000, host: 9000

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network "private_network", ip: "192.168.33.10"


  config.vm.provision "chef_solo" do |chef|
    chef.cookbooks_path = "provision/chef/cookbooks"
    chef.roles_path = "provision/chef/roles"
    chef.data_bags_path = "provision/chef/data_bags"

    chef.add_recipe "mysql::server"
    chef.add_recipe "mysql::client"
    chef.add_recipe "java"

    chef.json = {
      :java => {
        :jdk_version => "7"
      }
    }
  end

  config.vm.provision "shell", path: "provision/install_play.sh"

end
