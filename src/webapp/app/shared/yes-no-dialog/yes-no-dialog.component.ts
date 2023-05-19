import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'yes-no-dialog',
  templateUrl: 'yes-no-dialog.component.html',
})
export class YesNoDialog {
  constructor(
    public dialogRef: MatDialogRef<YesNoDialog>,
    @Inject(MAT_DIALOG_DATA) public data: {title: string, message: string},
  ) {}

  onResult(result: boolean): void {
    this.dialogRef.close(result);
  }
}
