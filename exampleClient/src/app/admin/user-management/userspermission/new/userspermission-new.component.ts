import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { UserspermissionService } from '../userspermission.service';
import { IUserspermission } from '../iuserspermission';
import { BaseNewComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

import { PermissionService } from 'src/app/admin/user-management/permission/permission.service';
import { UsersService } from 'src/app/admin/user-management/users/users.service';

@Component({
  selector: 'app-userspermission-new',
  templateUrl: './userspermission-new.component.html',
  styleUrls: ['./userspermission-new.component.scss'],
})
export class UserspermissionNewComponent extends BaseNewComponent<IUserspermission> implements OnInit {
  title: string = 'New Userspermission';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<UserspermissionNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public pickerDialogService: PickerDialogService,
    public userspermissionService: UserspermissionService,
    public errorService: ErrorService,
    public permissionService: PermissionService,
    public usersService: UsersService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(
      formBuilder,
      router,
      route,
      dialog,
      dialogRef,
      data,
      pickerDialogService,
      userspermissionService,
      errorService
    );
  }

  ngOnInit() {
    this.entityName = 'Userspermission';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.checkPassedData();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      permissionId: ['', Validators.required],
      revoked: [false],
      usersUsername: ['', Validators.required],
      permissionDescriptiveField: [''],
      usersDescriptiveField: [''],
    });

    this.fields = [
      {
        name: 'revoked',
        label: 'revoked',
        isRequired: false,
        isAutoGenerated: false,
        type: 'boolean',
      },
    ];
  }

  setAssociations() {
    this.associations = [
      {
        column: [
          {
            key: 'permissionId',
            value: undefined,
            referencedkey: 'id',
          },
        ],
        isParent: false,
        table: 'permission',
        type: 'ManyToOne',
        service: this.permissionService,
        label: 'permission',
        descriptiveField: 'permissionDescriptiveField',
        referencedDescriptiveField: 'displayName',
      },
      {
        column: [
          {
            key: 'usersUsername',
            value: undefined,
            referencedkey: 'username',
          },
        ],
        isParent: false,
        table: 'users',
        type: 'ManyToOne',
        service: this.usersService,
        label: 'users',
        descriptiveField: 'usersDescriptiveField',
        referencedDescriptiveField: 'username',
      },
    ];
    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let userspermission = this.itemForm.getRawValue();
    super.onSubmit(userspermission);
  }
}