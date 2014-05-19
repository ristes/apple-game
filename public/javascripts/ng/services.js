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
    'buyItem': {
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
    'tractor': [{
      id: 1,
      store: 'tractor',
      name: 'ObicenTraktor',
      url: '/public/images/game/TraktorStoreIcon.png'
    }, {
      id: 2,
      store: 'tractor',
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
      perHa: true,
      price: 1000,
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 2,
      name: 'M26',
      perHa: true,
      price: 0,
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      id: 3,
      name: 'MM106',
      perHa: true,
      price: 0,
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
      url: '/public/images/game/sadnici-icon.png'
    }, {
      id: 2,
      name: '3-4 grancinja',
      url: '/public/images/game/sadnici-icon.png'
    }, {
      id: 3,
      name: '8 grancinja',
      url: '/public/images/game/sadnici-icon.png'
    }],
    'fertilizer': [{
      id: 1,
      store: 'tractor',
      name: 'ObicenTraktor',
      url: '/public/images/game/TraktorStoreIcon.png'
    }, {
      id: 2,
      store: 'tractor',
      name: 'EkoTraktor',
      url: '/public/images/game/TraktorStoreIcon.png'
    }],
    'stores': [{
      name: 'tractor',
      url: '/public/images/game/sadnici-icon.png'
    }, {
      name: 'fertilizer',
      url: '/public/images/game/sadnici-icon.png'
    }, {
      name: 'spraying',
      url: '/public/images/game/sadnici-icon.png'
    }, {
      name: 'irrigation',
      url: '/public/images/game/sadnici-icon.png'
    }, {
      name: 'other',
      items: 'other',
      url: '/public/images/game/sadnici-icon.png'
    }],
  };
});

Game.factory('Operations', ['$resource', '$rootScope',
    function($resource, $rootScope) {
      return {
        'planting': [{
          ico: "prodavnica_home_icon",
          name: 'store',
          price: 0,
          duration: 0,
          requires: [],
          order: 1
        }, {
          ico: "korisnik_home_icon",
          name: 'plowing',
          price: 2000,
          duration: 20,
          order: 2
        }, {
          ico: "analiza_home_icon",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 3
        }, {
          ico: "akcii_home_icon",
          name: 'irrigation',
          price: 100,
          duration: -1,
          order: 4
        }, {
          ico: "analiza_home_icon",
          name: 'terrainAnalysis',
          price: 3000,
          duration: 0,
          order: 5
        }, {
          ico: "analiza_home_icon",
          name: 'planting',
          price: 0,
          duration: 0,
          requires: 'seedlings',
          order: 6
        }]
      };
    }]);