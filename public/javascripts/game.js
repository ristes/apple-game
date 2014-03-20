//pro tip: see also this work in progress by Hex http://jsfiddle.net/hexaust/HV4TX/
window.onload = function() {
	var atom = 64;

	var z = 0;
	var rows = 50;
	var fistRow = 10;
	var columns = 25;

	Crafty.init();
/*
	Crafty.e("2D, Canvas, Text").attr({
		x : 100,
		y : 100,
		z : 2000
	}).text(function() {
		return "My position is " + this._x
	});*/

	var gameStripes = new GameStripes();

	gameStripes.init();

	gameStripes.sky(0, 0);
	gameStripes.sky(895, 0);
	gameStripes.sky(1790, 0);

	var itemsPanel = new OperationsPanelComponent(64, 32, 1000, atom, columns);

	// size of the grass/stone block. Should be same as the sprite element used
	iso = Crafty.isometric.size(atom);
	for (var i = 0; i <= columns; i++) {
		for (var y = fistRow; y < rows; y++) {
			var which = Crafty.math.randomInt(0, 1);
			var topRow = false;
			if (y == fistRow) {
				topRow = true;
			}
			var z = (columns - i) + y * columns;

			var tile = gameStripes.grass(atom, z,
					OnClickCallbackManager.onClick, topRow);

			iso.place(i, y, 0, tile);
		}
	}
	Crafty.viewport.y = -32;
	Crafty.viewport.x = -64;

	Crafty.addEvent(this, Crafty.stage.elem, "asdf",
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
						itemsPanel.x += dx;
					}
					if (Crafty.viewport.y - dy > -320
							&& Crafty.viewport.y - dy < -32) {
						Crafty.viewport.y -= dy;
						itemsPanel.y += dy
					}
				}

				Crafty.addEvent(this, Crafty.stage.elem, "mousemove", scroll);
				Crafty.addEvent(this, Crafty.stage.elem, "mouseup", function() {
					Crafty.removeEvent(this, Crafty.stage.elem, "mousemove",
							scroll);
				});
			});
};