function readMedicationfile() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("medicationReminder.txt").done(function (datafile) {
        try {
            Windows.Storage.FileIO.readLinesAsync(datafile).done(function (lines) {
                lines.forEach(function (line) {
                    console.log(line);
                });
           });
        } catch (e) {
            console.log(e.message);
        }

    });
}