window.onload = function() {
	Crafty.init();

	Crafty.scene("loading", function() {

		// black background with some loading text
		Crafty.e("2D, DOM, Text").attr({
			w : 100,
			h : 20,
			x : 150,
			y : 120
		}).text("Loading").css({
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
			Crafty.scene("terrainShop");
		});
	});

};
