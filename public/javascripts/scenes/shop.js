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
			sp = Crafty.e("2D, DOM, " + data.image.name).attr({
				xoffset : 100,
				yoffset : 2,
				w : 45,
				h : 35
			});
			tt = this;
			this.text(data.name);
			this.childCmps.push(sp);
			this.addCmp(sp);
		};
		this.bind("Click", function() {
			// $.post("/terrainshop/buySeedling?seedlingId="
			// + this.modelData.entityId, function() {
			// Crafty.scene("shop");
			// });
			Crafty("ItemStoreText").each(function(i) {
				Crafty("ItemStoreText").get(i).remove();
			});
			Crafty("ItemStoreText").destroy();
			var id = this.modelData.entityId;
			var lst = Crafty.e("List").itemFn(function(data, x, y) {
				var item = Crafty.e("ItemStoreText").data(data).attr({
					x : x,
					y : y
				});

				return item;
			});
			lst.fx = function(item, x, y, padding) {
				return x + 170 + (item.order % 1) * (item.w + padding);
			};
			lst.fy = function(item, x, y, padding) {
				return y + Math.floor(item.order) * (item.h + padding);
			};
			lst.load("/storecontroller/showitems?storeId=" + id, 150, 30, 10,
					function() {
						loading.destroy();
					});
		});

	}
});

Crafty.c("ItemStoreLabel", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse");

		this.attr({
			w : 20,
			h : 15
		});
		this.css({
			"text-align" : "center",
			"font-size" : "x-large",
			"padding-top" : "5px",
		});

	},
	setText : function(t) {
		this.t = t;
		return this;
	},
	data : function(data) {
		this.text(this.t);
		return this;
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
			this.addCmp(Crafty.e("ItemStoreLabel").attr({
				xoffset : 5,
				yoffset : 15
			}).setText(data.price));
			sp = Crafty.e("2D, DOM, " + data.image.name).attr({
				xoffset : 100,
				yoffset : 2,
				w : 45,
				h : 35
			});
			tt = this;
			this.text(data.name);
			// this.text("$"+data.price);

			this.childCmps.push(sp);
			this.addCmp(sp);
		};

		this.bind("Click", function(data) {
			console.log(this.modelData);
			var item = Crafty.e("BuyItemComponent").data(this.modelData).attr({
				x : 400,
				y : 100
			});
			return item;
		});
		this.remove = function() {

			for (var i = 0; i < this.childCmps.length; i++) {
				this.childCmps[i].destroy();
			}

		};
	}
});

Crafty.c("BuyItemComponent", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");

		this.attr({
			w : 400,
			h : 300
		});
		this.css({
			"text-align" : "left",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "5px",
			"align" : "center",
			"background-color" : "grey"
		});

		this.display = function(data) {
			var title = Crafty.e("ItemStoreLabel").attr({
				xoffset : 150,
				yoffset : 20
			}).textFont({
				size : "20px",
				width : "bold"
			}).setText(data.name);

			var image = Crafty.e("2D, DOM, " + data.image.name).attr({
				xoffset : 165,
				yoffset : 70,
				w : 90,
				h : 70
			});

			var price = Crafty.e("ItemStoreLabel").attr({
				xoffset : 180,
				yoffset : 150
			}).setText(data.price).textFont({
				size : "15px"
			});

			var cancel_btn = Crafty.e("ItemStoreLabel").attr({
				xoffset : 200,
				yoffset : 250,
				w : 50,
				h : 20
			}).setText("Cancel").css({
				"cursor" : "pointer",
				"background-color" : "blue"
			}).textFont({
				size : "15px",
				width : "bold"
			});

			var buy_btn = Crafty.e("ItemStoreLabel").attr({
				xoffset : 200,
				yoffset : 200,
				w : 50,
				h : 20
			}).text("Buy").css({
				"cursor" : "pointer",
				"background-color" : "blue"
			}).textFont({
				size : "15px",
				width : "bold"
			});
			tt = this;
			buy_btn.bind("Click", function() {
				$.post("/storecontroller/buyItem?itemid=" + data.entityId
						+ "&quantity=1", function(result) {
					if (result.status == true) {
						Crafty("BuyItemComponent").destroy();
						Crafty("UserFrame").trigger("Invalidate", result);
						alert("KUPENO!");
					} else {
						alert("NEMA PARI!");
					}
				});
			});

			cancel_btn.bind("Click", function(tt) {
				Crafty("BuyItemComponent").destroy();
			});
			this.bind("Remove", function() {
				var numComp = this.childCmps.length;
				for (var i = 0; i < numComp; i++) {
					this.childCmps[0].destroy();
				}
			});
			this.addCmp(buy_btn);
			this.addCmp(title);
			this.addCmp(price);
			this.addCmp(cancel_btn);
			this.addCmp(image);
		};
	},
	data : function(data) {
		var text = "";
		text = data.price + " ден.";
		this.text(text);
		return this;
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
		var userframe = Crafty.e("UserFrame").data(AppleGame.farmer).attr({
			x : 800,
			y : 30
		});
		
		var weatherFrame = Crafty.e("List").itemFn(function(data, x, y) {
			var item = Crafty.e("WeatherFrame").data(data).attr({
				x : x,
				y : y
			});

			return item;
		});
		
		weatherFrame.fx = function(item, x, y, padding) {
			return x + item.w * item.order + padding;
		};
		weatherFrame.fy = function(item, x, y, padding) {
			return y;
		}; 

		weatherFrame.load("/weathercontroller/weatherforecast?fordays=4",950,30,10,function(){
			loading.destroy();
		}); 
		
		
		//loading.destroy();
	});

});
