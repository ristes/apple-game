
OperationComponent = function(oper, toolbar, atom, columns, field) {
	var imageLoaded = false;
	if (Crafty.asset(oper.icon.url)) {
		imageLoaded = true;
	}
	var sp = {};
	sp[oper.name] = [ 0, 0 ];
	Crafty.sprite(oper.icon.width, oper.icon.height, oper.icon.url, sp);
	if (imageLoaded) {
		Crafty(oper.name).each(function() {
			this.ready = true;
			this.trigger("Invalidate");
		});
	}

	var callback = window[oper.action];
	var inst = new callback(oper, atom, columns);
	inst.operation = oper;
	OnClickCallbackManager.registerCallback(oper.name, inst);

	this.button = Crafty.e("2D, Canvas, " + oper.name + ", Mouse").attr({
		x : toolbar.x,
		y : toolbar.y,
		z : toolbar.z,
		w : toolbar.w,
		h : toolbar.h
	}).bind('Click', function(e) {
		OnClickCallbackManager.activate(oper.name, field);
	});
};
