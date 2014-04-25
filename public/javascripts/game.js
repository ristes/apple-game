//pro tip: see also this work in progress by Hex http://jsfiddle.net/hexaust/HV4TX/

Crafty.scene("plantation", function() {
	var atom = 128;

	var z = 0;
	var rows = 30;
	var fistRow = 6;
	var columns = 25;

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

	Crafty.viewport.y = -32;
	Crafty.viewport.x = -64;
});