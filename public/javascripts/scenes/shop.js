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
			Crafty("ItemStoreText").each(function(i){
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
			lst.load("/storecontroller/showitems?storeId="+id, 150, 30, 10, function() {
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
		this.remove = function() {
			
			for (var i=0;i<this.childCmps.length;i++) {
				this.childCmps[i].destroy();
			}
			//
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
	lst.fx = function(item, x, y, padding) {
		return x + (item.order % 1) * (item.w + padding);
	};
	lst.fy = function(item, x, y, padding) {
		return y + Math.floor(item.order) * (item.h + padding);
	};
	lst.load("/storecontroller/all", 150, 30, 10, function() {
		loading.destroy();
	});

});

/*

 Crafty.scene("shop", function() {

 // for now only redirect to the plantation scene
 //Crafty.scene("terrainShop");

 ModelStore.getFromService("/storecontroller/all", function(data) {
 for (var i = 0; i < data.length; i++) {

 Crafty.sprite(540, 374, "/public/images/game/green_tractor.png", {
 store_item : [0, 0, 1, 1]
 });
 var btn = Crafty.e("2D, DOM, store_item, Mouse").attr({
 w : 60,
 h : 50,
 x : 200,
 y : 123 + 60 * i
 }).css({
 "cursor":"pointer",
 "title":data[i].name
 }).bind("Click", function() {
 if (!this.itemStore) {
 Crafty("item_store").destroy();
 Crafty("store_item").each(function(store) {
 Crafty("store_item").get(store).itemStore = undefined;
 });
 var itemStore = new ItemStore(260, this.store.y, this.store.entityId);
 this.itemStore = itemStore;
 // this.bind("MouseOut", function() {
 // this.unbind("MouseOut");
 //
 // //this.itemStore.removeComponent("item_store");
 // //this.itemStore.destroy();
 // //Crafty.removeComponent(itemStore);
 // });
 } else {
 console.log("Out");
 Crafty("item_store").destroy();
 this.itemStore = undefined;
 }
 });
 btn.store = data[i];
 btn.store.y = 123 + 60 * i;
 var buy = Crafty.e("2D, DOM, Text, Mouse").attr({
 w : 50,
 h : 14,
 x : 300,
 y : 143 + 60 * i
 }).text("buy").css({
 "text-align" : "center",
 "font-size" : "50px",
 "padding-top" : "5px",
 "cursor" : "pointer",
 "background-color" : "red",
 "valign" : "center"
 }).bind("Click", function() {

 });
 //buy.terrain = data[i];
 }
 });

 });
 */