Array.prototype.get = function(idx) {
	var regex = /([A-Z][a-z]+)-\d+/;
	if (regex.test(this[idx])) {
		return ModelStore.get(this[idx]);
	}
	return this[idx];
};

ModelStore = {
	store : {},
	classes : {},
	pending : {},
	get : function(id, onLoad) {
		if (ModelStore.store[id]) {
			if (onLoad)
				onLoad(ModelStore.store[id]);
			else
				return ModelStore.store[id];
		} else {
			var parts = id.split("-");
			$.getJSON("/modelstore/instance?clazz=models." + parts[0] + "&id="
					+ parts[1], function(data) {
				ModelStore.store[data.id] = data;
				if (onLoad)
					onLoad(data);
			});
		}
	},
	getFromService : function(url, onLoad) {

		var regex = /([A-Z][a-z]+)-\d+/;

		function fixPending(o) {
			function setVal(desc, val) {
				if (desc.i !== null) {
					desc.o[desc.p][desc.i] = val;
				} else {
					desc.o[desc.p] = val;
				}
			}

			function registerPending(obj, prop, index) {
				var key = obj[prop];
				if (index === 0 || index > 0) {
					key = key[index];
				}
				if (ModelStore.store[key]) {
					setVal({
						o : obj,
						p : prop,
						i : index
					}, ModelStore.store[key]);
					return;
				}

				if (ModelStore.pending[key]) {
					ModelStore.pending[key].push({
						o : obj,
						p : prop,
						i : index
					});
				} else {
					ModelStore.pending[key] = [ {
						o : obj,
						p : prop,
						i : index
					} ];
				}

			}

			if (ModelStore.pending[o.id]) {
				var arr = ModelStore.pending[o.id];
				for (var j = 0; j < arr.length; j++) {
					setVal(arr[j], o);
				}
				delete ModelStore.pending[o.id];
			}

			for ( var p in o) {
				if ('id' === p) {
					continue;
				}
				// if model entity
				if (typeof o[p] === 'string' && regex.test(o[p])) {
					registerPending(o, p, null);
				}
				// if array
				if (o[p] != null && typeof o[p] === 'object' && o[p].length
						&& o[p].length > 0) {
					if (regex.test(o[p][0])) {
						for (var i = 0; i < o[p].length; i++) {
							registerPending(o, p, i);
						}
					}
				}
				// if expanded model
				if (o[p] != null && typeof o[p] === 'object'
						&& regex.test(o[p].id)) {
					fixPending(o[p]);
					ModelStore.store[o[p].id] = o[p];
				}
			}
		}

		function getOrLoadProperty(from) {

			return function(prop) {

				if (prop === 'id') {
					return from[prop];
				}
				if (from[prop] && typeof from[prop] && from[prop].length > 0) {
					return from[prop];
				}
				if (regex.test(from[prop])) {
					return ModelStore.get(from[prop]);
				}
				return from[prop];
			};

		}

		if (!ModelStore.classes[url]) {
			$.getJSON(url, function(data) {
				ModelStore.classes[url] = data;
				for (var i = 0; i < data.length; i++) {
					data[i].get = getOrLoadProperty(data[i]);
					ModelStore.store[data[i].id] = data[i];
					fixPending(data[i]);
				}
				if (onLoad)
					onLoad(data);
			});

		} else {
			onLoad(ModelStore.classes[url]);
		}
	},
	getAll : function(clazz, onLoad) {
		var regex = /([A-Z][a-z]+)-\d+/;

		function fixPending(o) {
			function setVal(desc, val) {
				if (desc.i !== null) {
					desc.o[desc.p][desc.i] = val;
				} else {
					desc.o[desc.p] = val;
				}
			}

			function registerPending(obj, prop, index) {
				var key = obj[prop];
				if (index === 0 || index > 0) {
					key = key[index];
				}
				if (ModelStore.store[key]) {
					setVal({
						o : obj,
						p : prop,
						i : index
					}, ModelStore.store[key]);
					return;
				}

				if (ModelStore.pending[key]) {
					ModelStore.pending[key].push({
						o : obj,
						p : prop,
						i : index
					});
				} else {
					ModelStore.pending[key] = [ {
						o : obj,
						p : prop,
						i : index
					} ];
				}

			}

			if (ModelStore.pending[o.id]) {
				var arr = ModelStore.pending[o.id];
				for (var j = 0; j < arr.length; j++) {
					setVal(arr[j], o);
				}
				delete ModelStore.pending[o.id];
			}

			for ( var p in o) {
				if ('id' === p) {
					continue;
				}
				// if model entity
				if (typeof o[p] === 'string' && regex.test(o[p])) {
					registerPending(o, p, null);
				}
				// if array
				if (o[p] != null && typeof o[p] === 'object' && o[p].length
						&& o[p].length > 0) {
					if (regex.test(o[p][0])) {
						for (var i = 0; i < o[p].length; i++) {
							registerPending(o, p, i);
						}
					}
				}
				// if expanded model
				if (o[p] != null && typeof o[p] === 'object'
						&& regex.test(o[p].id)) {
					fixPending(o[p]);
					ModelStore.store[o[p].id] = o[p];
				}
			}
		}

		function getOrLoadProperty(from) {

			return function(prop) {

				if (prop === 'id') {
					return from[prop];
				}
				if (from[prop] && typeof from[prop] && from[prop].length > 0) {
					return from[prop];
				}
				if (regex.test(from[prop])) {
					return ModelStore.get(from[prop]);
				}
				return from[prop];
			};

		}

		if (!ModelStore.classes[clazz]) {
			$.getJSON("/modelstore/all?clazz=" + clazz, function(data) {
				ModelStore.classes[clazz] = data;
				for (var i = 0; i < data.length; i++) {
					data[i].get = getOrLoadProperty(data[i]);
					ModelStore.store[data[i].id] = data[i];
					fixPending(data[i]);
				}
				if (onLoad)
					onLoad(data);
			});

		} else {
			onLoad(ModelStore.classes[clazz]);
		}
	}
};
