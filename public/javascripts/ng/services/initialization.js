Game.factory('Terrain', ['$resource', function($resource) {

  var terrains = [{
    id: 1,
    name: 'aglesta',
    url: '/public/images/game/terrain/aglesta.png'
  }, {
    id: 2,
    name: 'lisesta',
    url: '/public/images/game/terrain/lisesta.png'
  }, {
    id: 3,
    name: 'prashkasta',
    url: '/public/images/game/terrain/prashkasta.png'
  }, {
    id: 4,
    name: 'prizmaticna',
    url: '/public/images/game/terrain/prizmaticna.png'
  }];

  var terrainSizes = [{
    size: 4,
    name: '4 Ha',
    price: 80000,
    url: '/public/images/game/pocva-prodavnica-icon.png'
  }, {
    size: 2,
    name: '2 Ha',
    price: 40000,
    url: '/public/images/game/pocva-prodavnica-icon.png'
  }, {
    size: 1,
    name: '1 Ha',
    price: 20000,
    url: '/public/images/game/pocva-prodavnica-icon.png'
  }, {
    size: 0.5,
    name: '0.5 Ha',
    price: 10000,
    url: '/public/images/game/pocva-prodavnica-icon.png'
  }];

  var res = $resource('', {}, {
    'buyTerrain': {
      method: 'POST',
      url: '/terrainshop/buyTerrain'
    }
  });

  return {
    terrains: terrains,
    terrainSizes: terrainSizes,
    buyTerrain: res.buyTerrain
  };

}]);

Game.factory('State', [function() {
  var state = {
    /**
     * Najaveniot farmer
     */
    farmer: null,
    diseases: null
  };

  var subscribers = {};

  return {
    subscribe: function(field, id, callback) {
      var topic = subscribers[field] || {};
      subscribers[field] = topic;
      if (topic.hasOwnProperty(id)) {
        throw exception('duplicate subscription id: ' + id);
    	  return;
      } else {
        topic[id] = callback;
        if (state[field]) {
          callback(state[field]);
        }
      }
    },
    unsubscribe: function(field, id) {
      delete subscribers[field][id];
    },
    get: function() {
      return state;
    },
    getByField: function(field) {
      return state[field];
    },
    set: function(field, data) {
      state[field] = data;
      angular.forEach(subscribers[field], function(callback, key) {
        callback(data);
      });
    }
  };

}]);