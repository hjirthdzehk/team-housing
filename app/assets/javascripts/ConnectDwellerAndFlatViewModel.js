var ConnectDwellerAndFlatViewModel = function (dwellers, flats) {
    var self = this;
    _.forEach(dwellers, function(dweller) {
        dweller.name += " " + dweller.surname
    });
    self.dwellers = ko.observableArray(dwellers);
    self.dweller = ko.observable();
    _.forEach(flats, function(flat) {
        flat.name = "Flat #" + flat.flatNumber
    });
    self.flats = ko.observableArray(flats);
    self.flat = ko.observable();

    self.click = function(){
        $.post('/api/bindPersonToFlat', {
            'personId': self.dweller().personId,
            'flatId': self.flat().flatId
        });
    };
};
