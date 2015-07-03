Game.factory('Fertilize', ['State', '$http', function(State, $http) {
    return {
    	recommend: function(callback) {
    		$http.get("RecommendationController/ferilize").then(function(data) {
    			callback(data.data);
    		});
    	},
        fertilize: function(fertilizer, callback) {
            $http({
                method: 'POST',
                url: "/fertilizationcontroller/fertilize",
                data: $.param(fertilizer),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function(res) {
                if (res.data && res.data.status == true && res.data.farmer) {
                    State.set('farmer', res.data.farmer);
                    if (typeof callback === 'function') {
                        callback();
                    }
                }
            });
        },
        "n":{"description":"Nitrogen is required for normal plant growth and development. If the quantity is not sufficient, then a smaller leaf surface is formed and there is reduced fruit tree growth. A nitrogen surplus may lead to the occurrence of chlorosis and necrosis of the leaf edges and may cause diminishing resistance against diseases and droughts, belated ripening and shorter fruit shelf life.  A nitrogen surplus can lead to pollution of underground waters with nitrates."},
        "p":{"description":"Phosphorus aids the growth of blooming buds and enhances fruit ripening. It has a positive effect on plant resistance against low temperatures and diseases. When there is an insufficient amount of phosphorus, tree growth decreases and the root system becomes underdeveloped. Excessive phosphorus concentrations result in slower fruit tree growth, occurrence of dark-brown spots on the leaf, faster fruit tree blossoming and ageing, and shorter vegetation. Through erosion, phosphorus surplus may lead to eutrophication of surface water. "}, 
        "k":{"description":"Potassium affects quantity and quality of the harvest. It aids the enhancement of fruit firmness, and it also plays a protective role in the case of a microelement surplus. It boosts plant resistance to conditions of soil salinization. When there is an insufficient amount of potassium, leaf folding occurs along with reduced resistance to high temperatures and lack of moisture. Potassium surplus is rarely noticed in farming land."},
        "ca":{"description":"Calcium deficiency is characterized by signs of chlorosis and necrosis in developing leaves. The number of reproductive organs is reduced and the fruit develops a bitter pit. The presence of an excessive amount of calcium in the soil has a negative consequence on the availability of certain microelements in the soil (B, Mn, Fe, Cu and Zn)."},
        "mg":{"description":"Magnesium, just like phosphorus, plays a vital role in photosynthesis and the formation of reproductive organs (blossoms and blossom elements). There is a distinct antagonism between magnesium and the surplus of calcium, potassium and manganese. In cases of greater deficiency, the leaf becomes orange, red or violet in color; necrotic spots emerge that spread gradually while leaf nerve structure retain the green color. Magnesium surplus causes lack of calcium and potassium."}
    }
}]);
