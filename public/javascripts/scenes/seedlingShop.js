Crafty.c("SeedlingText", {
	init : function() {
		this.childCmps = [];

		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");

		this.attr({
			w : 250,
			h : 40
		});
		this.css({
			"text-align" : "left",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "5px",
			"cursor" : "pointer",
			"background-color" : "grey"
		});
		this.display = function(data) {
			var sp = Crafty.e("2D, DOM, " + data.seedlingType.image.name).attr(
					{
						xoffset : 200,
						yoffset : 2,
						w : 25,
						h : 35
					});
			this.text(data.type.name + " (" + data.seedlingType.description
					+ ") - $" + data.price);

			this.addCmp(sp);
		};
		this.bind("Click", function() {
			$.post("/terrainshop/buySeedling?seedlingId="
					+ this.modelData.entityId, function() {
				Crafty.scene("shop");
			});
		});

	}
});

Crafty.scene("seedlingShop", function() {
	loading = Crafty.e("2D, DOM, Text").attr({
		w : 100,
		h : 20,
		x : 150,
		y : 40
	}).text("Loading Seedlings").css({
		"text-align" : "center",
		"font-size" : "50px"
	});

	var lst = Crafty.e("List").itemFn(function(data, x, y) {
		var item = Crafty.e("SeedlingText").data(data).attr({
			x : x,
			y : y
		});

		return item;
	});
	lst.fx = function(item, x, y, padding) {
		return x + (item.order % 2) * (item.w + padding);
	};
	lst.fy = function(item, x, y, padding) {
		return y + Math.floor(item.order / 2) * (item.h + padding);
	};
	lst.load("/terrainshop/allSeedlings", 150, 30, 10, function() {
		loading.destroy();
	});

});