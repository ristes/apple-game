ToolbarBuilder = function(x, y, z) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.k = 0;
	this.next = function() {
		var n = {
			x : (this.x + (this.k + 1) * 29 + this.k * 12),
			y : this.y + 17,
			z : this.z + 10,
			w : 12,
			h : 23
		};
		this.k++;
		return n;
	}
}

OperationsPanelComponent = function(x, y, z, atom, columns, field) {
	var toolbarBuilder = new ToolbarBuilder(x, y, z);

	Crafty.sprite(2287, 265, "/public/images/game/dolen-element.png", {
		itemsPanel : [ 0, 0, 1, 1 ]
	});

	this.panel = Crafty.e("2D, Canvas, itemsPanel, Mouse").attr({
		x : x,
		y : y,
		z : z,
		w : 570,
		h : 64
	});

	this.panel.buttons = [];

	var self = this;
	ModelStore.getAll("models.Operation", function(operations) {
		ModelStore.getAll("models.SpriteImage", function(sprites) {
			for (var j = 0; j < operations.length; j++) {
				console.log(operations[j].name)
				var operation = new OperationComponent(operations[j],
						toolbarBuilder.next(), atom, columns, field);

				self.panel.buttons.push(operation.button);
			}
		});
	});

}