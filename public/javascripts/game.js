//pro tip: see also this work in progress by Hex http://jsfiddle.net/hexaust/HV4TX/
window.onload = function() {
	var atom = 128;

	var z = 0;
	var rows = 30;
	var fistRow = 6;
	var columns = 25;

	Crafty.init();

	var gameStripes = new GameStripes();

	gameStripes.init();

	gameStripes.sky(0, 0);
	gameStripes.sky(895, 0);
	gameStripes.sky(1790, 0);
	gameStripes.bgLand(0, 200);
	gameStripes.bgLand(1366, 200);
	gameStripes.bgLand(0, 600);
	gameStripes.bgLand(1366, 600);
	gameStripes.bgLand(0, 1000);
	gameStripes.bgLand(1366, 1000);

	iso = Crafty.isometric.size(atom);
	var field = new Land(columns, rows, fistRow, iso, atom);
	field.init();

	var itemsPanel = new OperationsPanelComponent(64, 32, 1000, atom, columns,
			field);

	// size of the grass/stone block. Should be same as the sprite element used

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