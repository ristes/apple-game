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
      name: 'EkoTraktor',
      price: 250000,
      url: '/public/images/game/items/eco-tractor.png'
    }, {
      id: 2,
      store: 'tractor',
      name: 'ObicenTraktor',
      price: 150000,
      url: '/public/images/game/items/tractor.png'
    }],
    'terrain': [{
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
    }],
    'terrain-size': [{
      size: 20,
      name: '2 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 10,
      name: '1 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 5,
      name: '0.5 Ha',
      url: '/public/images/game/pocva-prodavnica-icon.png'
    }, {
      size: 2.5,
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
      url: '/public/images/game/appletypes/ajdared.png'
    }, {
      id: 2,
      name: 'CrvenDelises',
      url: '/public/images/game/appletypes/crven-delishes.png'
    }, {
      id: 3,
      name: 'ZlatenDelises',
      url: '/public/images/game/appletypes/zlaten-delishes.png'
    }, {
      id: 4,
      name: 'Jonagold',
      url: '/public/images/game/appletypes/jonagold.png'
    }, {
      id: 5,
      name: 'Mucu',
      url: '/public/images/game/appletypes/mucu.png'
    }, {
      id: 6,
      name: 'GreniSmit',
      url: '/public/images/game/appletypes/greni-smit.png'
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
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'NPK',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 2,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'ArskoGjubrivo',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 3,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'Ca',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 4,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'B',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 5,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'Mg',
      url: '/public/images/game/items/fertilizer.png'
    }],
    'irrigation': [{
      id: 1,
      store: 'irrigation',
      price: 100,
      name: 'BrazdiNavodnuvanje',
      url: '/public/images/game/items/grooves.png'
    }, {
      id: 2,
      store: 'irrigation',
      price: 100,
      perHa: true,
      name: 'KapkovoNavodnuvanje',
      url: '/public/images/game/items/drop.png'
    }, {
      id: 3,
      store: 'irrigation',
      price: 100,
      perHa: true,
      name: 'tensiometer',
      url: '/public/images/game/items/tensiometer.png'
    }],
    'other': [{
      id: 1,
      store: 'other',
      price: 100,
      name: 'bees',
      url: '/public/images/game/items/bees.png'
    }, {
      id: 2,
      store: 'other',
      price: 100,
      name: 'fridge',
      url: '/public/images/game/items/fridge.png'
    }, {
      id: 3,
      store: 'other',
      price: 100,
      perHa: true,
      name: 'protecting-net',
      url: '/public/images/game/items/protecting-net.png'
    }, {
      id: 4,
      store: 'other',
      price: 100,
      perHa: true,
      name: 'insurance',
      url: '/public/images/game/items/insurance.png'
    }],
    'spraying': [{
      id: 1,
      store: 'spraying',
      price: 100,
      perHa: true,
      name: 'insecticide',
      url: '/public/images/game/items/insecticide.png'
    }, {
      id: 2,
      store: 'spraying',
      price: 100,
      perHa: true,
      name: 'pheromone',
      url: '/public/images/game/items/pheromone.png'
    }, {
      id: 3,
      store: 'spraying',
      price: 100,
      perHa: true,
      name: 'BakarenFugicid',
      url: '/public/images/game/items/copper-fungicide.png'
    }, {
      id: 4,
      store: 'spraying',
      price: 100,
      perHa: true,
      name: 'BeloMaslo',
      url: '/public/images/game/items/white-oil.png'
    }],
    'stores': [{
      name: 'tractor',
      url: '/public/images/game/stores/tractor.png'
    }, {
      name: 'fertilizer',
      url: '/public/images/game/stores/fertilizer.png'
    }, {
      name: 'spraying',
      url: '/public/images/game/stores/sprays.png'
    }, {
      name: 'irrigation',
      url: '/public/images/game/stores/irrigation.png'
    }, {
      name: 'other',
      items: 'other',
      url: '/public/images/game/stores/other.png'
    }],
  };
});

Game.factory('Operations', ['$resource', '$rootScope',
    function($resource, $rootScope) {
      return {
        'planting': [{
          ico: "/public/images/game/operations/shop.png",
          name: 'store',
          price: 0,
          duration: 0,
          requires: [],
          order: 1
        }, {
          ico: "/public/images/game/operations/plowing.png",
          name: 'plowing',
          price: 2000,
          duration: 20,
          order: 2
        }, {
          ico: "/public/images/game/operations/fertilizing.png",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 3
        }, {
          ico: "/public/images/game/operations/irrigation.png",
          name: 'irrigation',
          price: 100,
          duration: -1,
          order: 4
        }, {
          ico: "/public/images/game/operations/terrain-analysis.png",
          name: 'terrainAnalysis',
          price: 3000,
          duration: 0,
          order: 5
        }, {
          ico: "/public/images/game/operations/planting.png",
          name: 'planting-done',
          price: 0,
          duration: 0,
          requires: 'seedlings',
          order: 6
        }]
      };
    }]);