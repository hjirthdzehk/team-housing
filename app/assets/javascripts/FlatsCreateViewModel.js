
var FlatsCreateViewModel = function() {
    var self = this;

    this.area = ko.observable('');
    this.flatNumber = ko.observable('');
    this.cladrId = ko.observable('');
    this.buildingId = ko.observable('');

    self.createFlat = function() {
        try {
            if  (self.area() !== '' && self.flatNumber !== ''
                && self.balance !== '' && self.cladrId !== ''
                && self.buildingId !== '') {
                    $.post('/api/flats', {
                        'area'          : self.area(),
                        'flatNumber'    : self.flatNumber(),
                        'balance'       : 0,
                        'cladrId'       : self.cladrId(),
                        'buildingId'    : self.buildingId()
                    }).then(function() {
                        self.area('');
                        self.flatNumber('');
                        self.cladrId('');
                        self.buildingId('');
                    });
                } else {
                    // TODO: Something was not set, make a warning
                }
        } catch (e) {
            // TODO: Make proper exception handling
            console.log("Something happened inside createFlat: ")
            console.log(e)
        }
    }
};