ItemStore = function(x,y,id) {
	ModelStore.getFromService("/storecontroller/showitems?storeId="+id,function(data) {
		for (var i=0;i<data.length;i++) {
			Crafty.sprite(540, 374,data[i].imageurl,{
				item_store:[0,0,1,1]
			});
			var image = Crafty.e("2D, DOM, item_store,Mouse").attr({
				x : x+10 + i*55,
				y : y+7,
				z : 1500,
				w : 45,
				h : 35
			}).css({
				"background" : "rgba(225, 225, 225, .8)",
				"padding" : "5px",
				"cursor" : "pointer",
				".hover background" : "rgba(225, 225, 225, .2)"
			}).bind("MouseOver",function() {
				
			});
		}
	});
};
