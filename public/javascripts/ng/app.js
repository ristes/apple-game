'use strict';

// Declare app level module which depends on filters, and services
var Game = angular.module('Game', ['Game.filters', 'Game.services',
    'Game.directives', 'ezfb']);

Game.config([
    '$routeProvider',
    '$translateProvider',
    '$httpProvider',
    '$locationProvider',
    'ezfbProvider',
    function($routeProvider, $translateProvider, $httpProvider,
            $locationProvider, ezfbProvider) {

    	ezfbProvider.setInitParams({
    	    appId: '671566419604666',
    	    version: 'v2.0'
    	  });
    	
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
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextWeek = function() {
    	  $day.nextWeek();
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextMonth = function() {
    	  $day.nextMonth();
    	  $rootScope.$emit("weather-hide");
      };
      
      $location.path("/");
    }]);
