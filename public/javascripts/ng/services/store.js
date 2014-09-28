Game.factory('Store', ['$resource', function($resource) {
  return $resource('', {}, {
    'buy': {
      method: 'POST',
      url: '/storecontroller/buyItem'
    },
    'buyItem': {
      method: 'POST',
      url: '/storecontroller/buyItem'
    },
    'buyBase': {
      method: 'POST',
      url: '/terrainshop/buyBase'
    },
    'buySeedling': {
      method: 'POST',
      url: '/terrainshop/buySeedling'
    }
  });
}]);