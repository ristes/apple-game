function GameStripes() {

	this.init = function() {
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

	}

	function land(size, type, spPos, zIndex, clickCallback, top) {
		var elem = (top ? "top" : "") + type;
		var land = Crafty.e("2D, Canvas, " + elem + " , Mouse")
		// z-index
		.attr('z', zIndex).attr({
			w : size,
			h : size
		})
		// used to check if the mouse is over the component
		.areaMap([ size / 2, 0 ], [ size, size / 4 ], [ size, 3 * size / 4 ],
				[ size / 2, size ], [ 0, 3 * size / 4 ], [ 0, size / 4 ])
		// on mouse click handler
		.bind("Click", clickCallback)
		// mouseover handler
		.bind("MouseOver", function() {
			this.sprite(spPos, 1, 1, 1);
		}).bind("MouseOut", function() {
			this.sprite(spPos, 0, 1, 1);
		});
		land._type = type;
		return land;
	}

	this.grass = function(size, zIndex, clickCallback, topRow) {
		return land(size, "grass", 0, zIndex, clickCallback, topRow);
	}

	this.stone = function(size, zIndex, clickCallback, topRow) {
		return land(size, "stone", 1, zIndex, clickCallback, topRow);
	}

	this.sky = function(x, y) {
		return Crafty.e("2D, Canvas, sky").attr({
			x : x,
			y : y
		});
	}

}