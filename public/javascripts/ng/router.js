Game.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {
    templateUrl: '/public/_views/index.html'
  }).when('/login', {
    templateUrl: '/public/_views/login.html',
    controller: 'LoginController'
  }).when('/buy_tractor', {
    templateUrl: '/public/_views/buy-tractor.html',
    controller: 'StoreController'
  }).when('/buy_terrain', {
    templateUrl: '/public/_views/buy-terrain.html',
    controller: 'BuyTerrainController'
  }).when('/buy_seedlings', {
    templateUrl: '/public/_views/buy-seedlings.html',
    controller: 'BuySeadlingsController'
  }).when('/buy_base', {
    templateUrl: '/public/_views/buy-base.html',
    controller: 'BuyBaseController'
  }).when('/plantation', {
    templateUrl: '/public/_views/plantation.html',
    controller: 'PlantingStateController'
  }).when('/plowing', {
	templateUrl: '/public/_views/plowing_scene.html',
	controller: 'PlowingSceneController' 
  }).when('/growing', {
    templateUrl: '/public/_views/plantation.html',
    controller: 'GrowingStateController'
  }).when('/quiz', {
	    templateUrl: '/public/_views/plantation.html',
	    controller: 'QuizController'
	  }).otherwise({
    redirectTo: '/'
  });

}]);