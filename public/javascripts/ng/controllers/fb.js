(function(){
	Game.controller('fbController', ['ezfb', '$http', '$timeout', '$location', function(ezfb, $http, $timeout, $location){
		var self=this;
		
		self.fbId=0;
		self.preloadedInviteFriends=true;
		
		self.init=function(){
			ezfb.getLoginStatus(function(r){
				if(r.status=='connected'){
					$timeout(function(){
						if($location.url().indexOf('plantation')>0 ||
								$location.url().indexOf('growing')>0){
							self.preloadedInviteFriends=false;
						}
					}, 2000);
				}
			});
		};
		
		self.initDialog=function(val){
			self.preloadedInviteFriends=val;
		};
		
		self.init();
	}])
	
	.controller('fbInviteFriends', ['ezfb', '$http', '$timeout', function(ezfb, $http, $timeout){
		var self=this;
		
		self.friends=new Array();
		self.checkedFriends=new Array();
		self.dummy=0;
		
		self.init=function(){
			ezfb.getLoginStatus(function(res){
				if(res.status=='connected'){
					self.getFriends();
				}
			});
		};
		
		self.checkFriend=function(item){
			var index=self.checkedFriends.indexOf(item);
			if(index==-1){
				self.checkedFriends.push(item);
			}
			else{
				self.checkedFriends.splice(index, 1);
			}
		};
		
		self.inviteFriends=function(){
			ezfb.ui({method: 'apprequests',
			     to: self.checkedFriends.join(),
			     title: 'My Great Invite',
			     message: 'Check out this Awesome App!',
			   }, function(res){
				   self.checkedFriends=new Array();
				   self.getFriends();
			   });
		};
		
		self.getFriends=function(){
			ezfb.api("/me/invitable_friends", function(res){
				self.friends=res.data;
			});
		};
		
		self.init();
	}]);
	
}());