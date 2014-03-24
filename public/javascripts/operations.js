var operations = [ {
	name : 'plant',
	icon : {
		url : "/public/images/game/sadnica.png",
		width : 70,
		height : 128
	},
	plant : {
		url : "/public/images/game/sadnica.png",
		width : 70,
		height : 128,
		ratio : {
			w : 1 / 4,
			h : 1 / 2
		}
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'youngPlant',
	icon : {
		url : "/public/images/game/mala-sadnica.png",
		width : 351,
		height : 545
	},
	plant : {
		url : "/public/images/game/mala-sadnica.png",
		width : 351,
		height : 545,
		ratio : {
			w : 1,
			h : 1
		},
		areaMap : [ [ 0, 0 ], [ 1, 0 ], [ 1, 0.6 ], [ 0, 0.6 ] ]
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'bigPlant',
	icon : {
		url : "/public/images/game/golema-sadnica.png",
		width : 543,
		height : 720
	},
	plant : {
		url : "/public/images/game/golema-sadnica.png",
		width : 543,
		height : 720,
		ratio : {
			w : 1,
			h : 3 * 1 / 2
		},
		areaMap : [ [ 0, 0 ], [ 1, 0 ], [ 1, 0.9 ], [ 0, 0.9 ] ]
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'applePlant',
	icon : {
		url : "/public/images/game/rodna-sadnica.png",
		width : 802,
		height : 941
	},
	plant : {
		url : "/public/images/game/rodna-sadnica.png",
		width : 802,
		height : 941,
		ratio : {
			w : 1,
			h : 3 * 1 / 2
		},
		areaMap : [ [ 0, 0 ], [ 1, 0 ], [ 1, 0.9 ], [ 0, 0.9 ] ]
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'prskanje',
	icon : {
		url : "/public/images/game/prskanje.png",
		width : 128,
		height : 128
	},
	effect : {
		url : "/public/images/game/prskanje.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		},
		speed : 30,
		duration : 30,
		frameNumber : 3,
		shouldDestroy : true
	},
	ground : {
		url : "/public/images/game/zemja.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		}
	},
	plant : {
		url : "/public/images/game/rodna-sadnica.png",
		width : 802,
		height : 941,
		ratio : {
			w : 1,
			h : 3 * 1 / 2
		},
		areaMap : [ [ 0, 0 ], [ 1, 0 ], [ 1, 0.9 ], [ 0, 0.9 ] ]
	},
	action : 'AddEffectOnTop'
}, {
	name : 'green_tractor',
	duration : 200,
	icon : {
		url : "/public/images/game/green_tractor.png",
		width : 540,
		height : 374,
		ratio : {
			w : 0.75,
			h : 0.5
		}
	},
	ground : {
		url : "/public/images/game/zemja.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		}
	},
	action : 'ProgressCallback'
}, {
	name : 'masinskoPrskanje',
	duration : 100,
	icon : {
		url : "/public/images/game/prskanje1.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		}
	},
	effect : {
		url : "/public/images/game/prskanje1.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		},
		speed : 30,
		duration : 30,
		frameNumber : 3,
		shouldDestroy : true
	},
	ground : {
		url : "/public/images/game/zemja.png",
		width : 128,
		height : 128,
		ratio : {
			w : 1,
			h : 1
		}
	},
	plant : {
		url : "/public/images/game/rodna-sadnica.png",
		width : 802,
		height : 941,
		ratio : {
			w : 1,
			h : 3 * 1 / 2
		},
		areaMap : [ [ 0, 0 ], [ 1, 0 ], [ 1, 0.9 ], [ 0, 0.9 ] ]
	},
	action : 'ProgressCallback'
} ];

OperationComponent = function(oper, toolbar, atom, columns, field) {
	var sp = {};
	sp[oper.name] = [ 0, 0 ];

	Crafty.sprite(oper.icon.width, oper.icon.height, oper.icon.url, sp);

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
}
