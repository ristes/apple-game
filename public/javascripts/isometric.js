//pro tip: see also this work in progress by Hex http://jsfiddle.net/hexaust/HV4TX/
window.onload = function() {
	Crafty.init();


	Crafty.sprite(128, "/public/images/sprite.png", {
		grass : [ 0, 0, 1, 1 ], // x, y, w, h
		stone : [ 1, 0, 1, 1 ]
	});
	Crafty.sprite(128, "/public/images/topland.png", {
		topgrass : [ 0, 0, 1, 1 ], // x, y, w, h
		topstone : [ 1, 0, 1, 1 ]
	});

	Crafty.sprite(895, 440, "/public/images/sky.png", {
		sky : [ 0, 0, 1, 1 ]
	});

	Crafty.e("2D, DOM, sky").attr({
		x : 0,
		y : 0
	});
	Crafty.e("2D, DOM, sky").attr({
		x : 895,
		y : 0
	});
	Crafty.e("2D, DOM, sky").attr({
		x : 1790,
		y : 0
	});

	// size of the grass/stone block. Should be same as the sprite element used
	iso = Crafty.isometric.size(128);
	var z = 0;
	for (var i = 20; i >= 0; i--) {
		for (var y = 10; y < 30; y++) {
			var which = Crafty.math.randomInt(0, 1);
			var pref = "";
			if (y == 10) {
				pref = "top";
			}
			var tile = Crafty.e(
					"2D, DOM, " + (!which ? pref + "grass" : pref + "stone")
							+ ", Mouse")
			// z-index
			.attr('z', i + 1 * y + 1)
			// used to check if the mouse is over the component
			.areaMap([ 64, 0 ], [ 128, 32 ], [ 128, 96 ], [ 64, 128 ],
					[ 0, 96 ], [ 0, 32 ])
			// on mouse click handler
			.bind("Click", function(e) {
				// destroy on right click
				// right click seems not work in Mac OS
				// delete it
				console.log(e.button);
				// this.destroy();
			}).bind("MouseOver", function() {
				if (this.has("grass")) {
					this.sprite(0, 1, 1, 1);
				} else {
					this.sprite(1, 1, 1, 1);
				}
			}).bind("MouseOut", function() {
				if (this.has("grass")) {
					this.sprite(0, 0, 1, 1);
				} else {
					this.sprite(1, 0, 1, 1);
				}
			});

			iso.place(i, y, 0, tile);
		}
	}
	Crafty.viewport.y = -32;
	Crafty.viewport.x = -64;

	Crafty.addEvent(this, Crafty.stage.elem, "mousedown",
			function(e) {
				if (e.button > 1)
					return;
				var base = {
					x : e.clientX,
					y : e.clientY
				};

				function scroll(e) {
					var dx = base.x - e.clientX, dy = base.y - e.clientY;
					base = {
						x : e.clientX,
						y : e.clientY
					};
					if (Crafty.viewport.x - dx > -1280
							&& Crafty.viewport.x - dx < -64) {
						Crafty.viewport.x -= dx;
					}
					if (Crafty.viewport.y - dy > -320
							&& Crafty.viewport.y - dy < -32) {
						Crafty.viewport.y -= dy;
					}
				}

				Crafty.addEvent(this, Crafty.stage.elem, "mousemove", scroll);
				Crafty.addEvent(this, Crafty.stage.elem, "mouseup", function() {
					Crafty.removeEvent(this, Crafty.stage.elem, "mousemove",
							scroll);
				});
			});
};