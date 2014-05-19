AddOrRelpacePlantCallback = ActionCallback.extend(function(operation, atom,
		zLevles) {

	var self = this;
	this.size = atom;
	this.operation = operation;

	this.initSprites(operation);

	this.onClick = function(e, ground) {
		if (ground.plant) {
			ground.plant.destroy();
		}

		var plant = self.createPlant(self.operation, atom, ground);
		if (plant) {
			plant.attr({
				z : ground._z + zLevles
			});

		}
	};
});