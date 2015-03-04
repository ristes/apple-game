
GameDirectives
		.directive(
				'sideMenu',
				[
						'jQuery',
						'$window',
						'BoughtItems',
						'ExpertAdvice',
						'MonthOperations',
						function($, $window, BoughtItems, ExpertAdvice,
								MonthOperations) {

							return {
								restrict : 'E',
								transclude : true,
								scope : {
									weather : '=',
									actions : '='
								},
								link : function(scope, element, attrs, ctrl,
										transclude, formCtrl) {

									scope.isForbidden = function(farmer, operation) {
										var opers = MonthOperations.forbiddenOperation[farmer.gameDate.weatherType.name];
										var isFound = false;
										angular.forEach(opers, function(value) {
											if (value === operation) {
												isFound = true;
											}
										});
										return isFound;
									}
									;

									scope.itemClick = function(a) {
										if (!scope.isForbidden(scope.$root.farmer, a.name)) {
											scope.$root.$emit('operation-'
													+ a.name, a);
										} else {
											ExpertAdvice
													.setImportantAdvice('Depending of weather conditions, you could not execute the operation.');
										}
									}
									scope.bgw = 0;
									scope.bgh = 0;

									scope.visible = false;

									scope.executeItem = function(item) {
										console.log(item);
									}

									scope.show = function() {
										scope.$root.$emit("shop-hide");
										scope.bgw = $window.innerWidth;
										scope.bgh = $window.innerHeight;
										scope.visible = true;
									}
									scope.hide = function() {
										scope.bgw = 0;
										scope.bgh = 0;
										scope.visible = false;
									}

									var un = scope.$root.$on("side-hide",
											function() {
												scope.hide();
											});

									scope.$on('$destroy', function() {
										if (un) {
											un();
										}
									})

									scope.onClick = function() {
										if (scope.visible) {
											scope.hide();
										} else {
											scope.show();
										}
									}

									scope.onHover = function(item) {
										ExpertAdvice
												.setAdvice(item.description);
									};

									scope.onLeave = function(item) {
										ExpertAdvice.hide();
									}
								},
								templateUrl : '/public/templates/side-menu.html'
							};

						} ])
		.directive(
				'resumeDialog',
				[
						'jQuery',
						'$window',
						'$modal',
						'$day',
						'State',
						'Resume',
						function($, $window, $modal, day, State, Resume) {

							return {
								restrict : 'E',
								transclude : true,
								scope : {},
								link : function(scope, element, attrs, ctrl,
										transclude, formCtrl) {

									var windowTemplateUrl = '/public/templates/resume-dialog.html';
									scope.visible = false;
									scope.$root.$on("resume-open", function() {
										Resume.load();
										State.subscribe('resume',
												'resumeDirective', function(
														data) {
													scope.visible = true;
													scope.resumeData = data;
												});
										scope.visible = false;
										scope.$root.$on("resume-close",
												function() {
													scope.bgw = 0;
													scope.bgh = 0;
													Resume.setSeen();
												});
									});

									scope.close = function() {
										scope.visible = false;
										scope.$root.$emit("resume-close");
									}

									scope.$root
											.$watch(
													"day.isNewSeason",
													function(n) {
														if (!n)
															return;
														if (n === true) {
															scope.$root
																	.$emit("resume-open");
														}

													})

								},
								templateUrl : '/public/templates/resume-dialog.html'
							};

						} ]);