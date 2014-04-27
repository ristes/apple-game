Crafty.scene("shop", function() {
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
				console.log("buy: " + this.terrain.description);
			});
			//buy.terrain = data[i];
		}
	});
});
