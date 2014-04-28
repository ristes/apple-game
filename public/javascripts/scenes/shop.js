Crafty.c("ShopItemText", {
	init : function() {
		this.childCmps = [];

		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");

		this.attr({
			w : 150,
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
			var sp = Crafty.e("2D, DOM, " + data.image.name).attr({
				xoffset : 100,
				yoffset : 2,
				w : 45,
				h : 35
			});
			this.text(data.name);
			this.childCmps.push(sp);
			this.addCmp(sp);
		};
		this.bind("Click", function() {

			Crafty("ItemStoreText").destroy();

			var id = this.modelData.entityId;

			var lst = Crafty.e("List").itemFn(function(data, x, y) {
				var item = Crafty.e("ItemStoreText").data(data).attr({
					x : x,
					y : y
				});

				return item;
			});

			lst.load("/storecontroller/showitems?storeId=" + id, 320, 30, 10,
					function() {
						loading.destroy();
					});
		});

	}
});

Crafty.c("ItemStoreText", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");

		this.attr({
			w : 150,
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
			var sp = Crafty.e("2D, DOM, " + data.image.name).attr({
				xoffset : 100,
				yoffset : 2,
				w : 45,
				h : 35
			});
			this.text(data.name);
			// this.text("$"+data.price);

			this.addCmp(sp);
		};
	}
});

Crafty.scene("shop", function() {
	loading = Crafty.e("2D, DOM, Text").attr({
		w : 100,
		h : 20,
		x : 150,
		y : 40
	}).text("Loading Shop").css({
		"text-align" : "center",
		"font-size" : "50px"
	});

	var lst = Crafty.e("List").itemFn(function(data, x, y) {
		var item = Crafty.e("ShopItemText").data(data).attr({
			x : x,
			y : y
		});

		return item;
	});

	lst.load("/storecontroller/all", 150, 30, 10, function() {
		loading.destroy();
	});

});
