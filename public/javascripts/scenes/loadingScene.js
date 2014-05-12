var AppleGame = {
	stateChangedListeners : [],
	analysisPrice : 1000,
	onFarmerStateChanged : function(fn) {
		if (typeof fn === "function") {
			this.stateChangedListeners.push(fn);
		}
	},
	changeState : function(farmer) {
		this.farmer = farmer;
		ModelStore.add(farmer);
	},
	buy : function(url, amount, msg) {
		var pay = msg && confirm(msg);
		if (pay) {
			if (AppleGame.farmer.balans > amount) {
				$.post(url,function(data) {
								
					if (amount === 0 || AppleGame.farmer.balans !== data.balans) {
						AppleGame.changeState(data);
						Crafty.scene(AppleGame.farmer.currentState);
					} else {
						alert("Not enough balans for the user!!!");
					}
					});

			} else {
				alert("Not enough balans for the user!!!");
			}
		}
	},
	setWeather : function(data) {
		this.weather = data;
		ModelStore.add(data);
	}

};
