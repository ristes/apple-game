'use strict';

// Declare app level module which depends on filters, and services
var Game = angular.module('Game', ['Game.filters', 'Game.services',
    'Game.directives']);

Game.config([
    '$routeProvider',
    '$translateProvider',
    '$httpProvider',
    '$locationProvider',
    function($routeProvider, $translateProvider, $httpProvider,
            $locationProvider) {

      // Initialize angular-translate
      $translateProvider.useStaticFilesLoader({
        prefix: '/public/i18n/',
        suffix: '.json'
      });

      $translateProvider.preferredLanguage('en');

      $translateProvider.useCookieStorage();

    }]);

Game.run(['$rootScope', '$location', '$farmer', '$items','$day','$plantation','StoreItems',
    function($rootScope, $location, $farmer, $items, $day, $plantation, $StoreItems) {
	  $StoreItems.load();
      $farmer.load();
      $day.load($farmer);
      $plantation.load();
      $rootScope.visible=false;
      $rootScope.next = function() {
    	  $day.next();
    	  $items.load();
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextWeek = function() {
    	  $day.nextWeek();
    	  $items.load();
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextMonth = function() {
    	  $day.nextMonth();
    	  $items.load();
    	  $rootScope.$emit("weather-hide");
      };
      $location.path("/");
    }]);
