'use strict';

/* Services */
angular.module('Game.services', []).value('version', '0.1');

Game.factory('Crafty', [function() {
  return Crafty;
}]);

Game.factory('ModelStore', function() {
  return ModelStore;
});

Game.factory('jQuery', function() {
  return jQuery;
});

Game.factory('Store', ['$resource', function($resource) {
  return $resource('', {}, {
    'buy': {
      method: 'POST',
      url: '/storecontroller/buyItem'
    },
    'buyTerrain': {
      method: 'POST',
      url: '/terrainshop/buyTerrain'
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

Game.factory('StoreItems', function($resource) {
  return {
    'traktor': [{
      id: 1,
      name: 'ObicenTraktor',
      url: '/public/images/game/TraktorStoreIcon.png'
    }, {
      id: 2,
      name: 'EkoTraktor',
      url: '/public/images/game/TraktorStoreIcon.png'
    }],
    'terrain': [{
      id: 1,
      name: 'Glinesta',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 2,
      name: 'Humusna',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 3,
      name: 'Pesokliva',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 4,
      name: 'Obicna',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }],
    'terrain-size': [{
      size: 2,
      name: '2 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 1,
      name: '1 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 0.5,
      name: '0.5 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 0.25,
      name: '0.25 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }],
    'base': [{
      id: 1,
      name: 'M9',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 2,
      name: 'M26',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 3,
      name: 'MM106',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }],
    'apple-type': [{
      id: 1,
      name: 'Ajdaret',
      url: '/public/images/game/jabolko.png'
    }, {
      id: 2,
      name: 'CrvenDelises',
      url: '/public/images/game/jabolko.png'
    }, {
      id: 3,
      name: 'ZlatenDelises',
      url: '/public/images/game/jabolko.png'
    }, {
      id: 4,
      name: 'Jonagold',
      url: '/public/images/game/jabolko.png'
    }, {
      id: 5,
      name: 'Mucu',
      url: '/public/images/game/jabolko.png'
    }, {
      id: 6,
      name: 'GreniSmit',
      url: '/public/images/game/jabolko.png'
    }],
    'seedling-type': [{
      id: 1,
      name: '0 grancinja',
      url: '/public/images/game/grance.png'
    }, {
      id: 2,
      name: '3-4 grancinja',
      url: '/public/images/game/grance.png'
    }, {
      id: 3,
      name: '8 grancinja',
      url: '/public/images/game/grance.png'
    }],
  };
});
