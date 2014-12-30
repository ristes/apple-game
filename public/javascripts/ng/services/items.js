Game.factory('BoughtItems', ['$http', 'State', function($http, State) {

  var storeItems = {};
  var boughtItems = [];
  var namedItems = {};
  var items = [];
  return {
    load: function(callback) {
      var res = $http.get("/storecontroller/myitems");
      res.success(function(data) {
        storeItems = {};
        State.set('boughtItems', data);
        boughtItems = data || [];
        for (var i = 0; i < boughtItems.length; i++) {
          var item = data[i];
          var store = item.store;
          storeItems[store] = storeItems[store] || [];
          storeItems[store].push(data[i]);
          storeItems[store].sort(function(a,b) {
        	  return b.id-a.id;
          });
          namedItems[item.name] = item;
        }
        if (callback && typeof callback === 'function') {
          callback(data);
        }
      });
    },
    add: function(key, val) {
      storeItems[key] = storeItems[key] || [];
      var vals = storeItems[key];
      // todo: check for duplicates
      vals.push(val);
      boughtItems.push(val);
    },
    check: function(key) {
      return storeItems[key] && storeItems[key].length > 0;
    },
    get: function(key) {
      return storeItems[key];
    },
    getByName: function(name) {
      return namedItems[name];
    },
    use: function(key, item) {
      storeItems[key].pop();

      function remove(arr) {
        var results = [];
        var removed = false;
        angular.forEach(arr, function(val) {

          if (!removed && val.store == key) {
            if (!item || val.id == item.id) {
              removed = true;
            } else {
              results.push(val);
            }
          } else {
            results.push(val);
          }
        });
        return results;
      }
      boughtItems = remove(boughtItems);
      storeItems[key] = remove(storeItems[key]);
    },
    all: function() {
      var results = [];
      angular.forEach(storeItems, function(val) {
        angular.forEach(val, function(v) {
          results.push(v);
        });
      });
      return results;
    }

  };
}]);
