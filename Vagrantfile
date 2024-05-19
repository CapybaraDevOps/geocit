######################## Variables ########################
BOX_IMAGE = "ubuntu/jammy64"			# Ubuntu 22.04
IP = "192.168.100.10"					# Private subnet
RAM = "4096"							# RAM
CPUS = 2								# CPU
######################## Variables ########################

Vagrant.configure(2) do |config|
  config.vm.define "db" do |db|
    db.vm.box = BOX_IMAGE
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "#{IP.succ}"
    db.vm.provider "virtualbox" do |vb|
      vb.name = "db"
      vb.gui = false
      vb.memory = RAM
      vb.cpus = CPUS
    end
    db.vm.provision "shell", privileged: false, path: "setup_db.sh"
  end

  config.vm.define "app" do |app|
    app.vm.box = BOX_IMAGE
    app.vm.hostname = "app"
    app.vm.network "private_network", ip: "#{IP}"
    app.vm.provider "virtualbox" do |vb|
      vb.name = "app"
      vb.gui = false
      vb.memory = RAM
      vb.cpus = CPUS
    end
    app.vm.provision "file", source: "./.env", destination: "~/"
    app.vm.provision "shell", privileged: false,  path: "setup_app.sh"
  end
end