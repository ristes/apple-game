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
      name: 'ecoTractor',
      price: 250000,
      url: '/public/images/game/items/eco-tractor.png'
    }, {
      id: 2,
      store: 'tractor',
      name: 'tractor',
      price: 150000,
      url: '/public/images/game/items/tractor.png'
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
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'npk',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 2,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'animal',
      url: '/public/images/game/items/fertilizer.png'
    }, {
      id: 3,
      store: 'fertilizer',
      perHa: true,
      price: 100,
      name: 'ca',
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
      name: 'mg',
      url: '/public/images/game/items/fertilizer.png'
    }],
    'irrigation': [{
      id: 1,
      store: 'irrigation',
      price: 100,
      name: 'grooves',
      url: '/public/images/game/items/grooves.png'
    }, {
      id: 2,
      store: 'irrigation',
      price: 100,
      perHa: true,
      name: 'drop',
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
      name: 'copper-fungicide',
      url: '/public/images/game/items/copper-fungicide.png'
    }, {
      id: 4,
      store: 'spraying',
      price: 100,
      perHa: true,
      name: 'white-oil',
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
          name: 'planting',
          price: 0,
          duration: 0,
          requires: 'seedlings',
          order: 6
        }]
      };
    }]);