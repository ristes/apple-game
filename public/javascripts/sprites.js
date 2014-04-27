function GameStripes() {

	this.init = function() {

		Crafty.sprite(895, 440, "/public/images/sky.png", {
			sky : [ 0, 0, 1, 1 ]
		});

		Crafty.sprite(1366, 418, "/public/images/game/bg_grass.png", {
			bgLand : [ 0, 0, 1, 1 ]
		});

	};

	this.bgLand = function(x, y) {
		return Crafty.e("2D, Canvas, bgLand").attr({
			x : x,
			y : y
		});
	};

	this.sky = function(x, y) {
		return Crafty.e("2D, Canvas, sky").attr({
			x : x,
			y : y
		});
	};

}