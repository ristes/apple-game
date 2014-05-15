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
    }
  });
}]);