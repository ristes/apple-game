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
				$
						.post(
								url,
								function(data) {
									if (amount === 0
											|| AppleGame.farmer.balans !== data.balans) {
										AppleGame.changeState(data);
										Crafty
												.scene(AppleGame.farmer.currentState);
									} else {
										alert("Not enough balans for the user!!!");
									}
								});
			} else {
				alert("Not enough balans for the user!!!");
			}
		}
	}

};

window.onload = function() {
	Crafty.init();

	Crafty.scene("loading", function() {

		// black background with some loading text
		Crafty.e("2D, DOM, Text").attr({
			w : 100,
			h : 20,
			x : 150,
			y : 120
		}).text("Loading scenes").css({
			"text-align" : "center",
			"font-size" : "50px"
		});
	});

	// automatically play the loading scene
	Crafty.scene("loading");
	var images = [ "/public/images/sky.png",
			"/public/images/game/bg_grass.png", "/public/images/sprite.png",
			"/public/images/game/dolen-element.png" ];

	ModelStore.getAll("models.SpriteImage", function(sprites) {
		for (var j = 0; j < sprites.length; j++) {
			images.push(sprites[j].url);

			var sp = {};
			sp[sprites[j].name] = [ 0, 0 ];
			Crafty.sprite(sprites[j].width, sprites[j].height, sprites[j].url,
					sp);
		}
		Crafty.load(images, function() {
			$.post("/AuthController/farmer", function(data) {
				AppleGame.changeState(data);
				if (AppleGame.farmer.currentState) {
					Crafty.scene(AppleGame.farmer.currentState);
				} else {
					Crafty.scene("terrainShop");
				}
			});
		});
	});

};
