import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { AdminLoginComponent } from './components/admin/admin-login/admin-login.component';
import { LandingComponent } from './components/landing/landing.component';
import { LayoutComponent } from './components/layout/layout.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { WalletComponent } from './components/wallet/wallet.component';
import { BankAccountComponent } from './components/bank-account/bank-account.component';
import { BillPaymentComponent } from './components/bill-payment/bill-payment.component';
import { BeneficiaryComponent } from './components/beneficiary/beneficiary.component';
import { TransactionComponent } from './components/transaction/transaction.component';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { AdminOverviewComponent } from './components/admin/admin-overview/admin-overview.component';
import { AdminCustomersComponent } from './components/admin/admin-customers/admin-customers.component';
import { AdminWalletsComponent } from './components/admin/admin-wallets/admin-wallets.component';
import { AdminBankAccountsComponent } from './components/admin/admin-bank-accounts/admin-bank-accounts.component';
import { AdminBeneficiariesComponent } from './components/admin/admin-beneficiaries/admin-beneficiaries.component';
import { AdminBillPaymentsComponent } from './components/admin/admin-bill-payments/admin-bill-payments.component';
import { AdminTransactionsComponent } from './components/admin/admin-transactions/admin-transactions.component';

export const routes: Routes = [
  { path: '', component: LandingComponent, pathMatch: 'full' },
  {
    path: 'login',
    component:LoginComponent
  },
  {
    path: 'signup',
    component:SignupComponent
  },
  {
    path: 'admin-login',
    component:AdminLoginComponent
  },
  {
    path: '',
    canActivate: [authGuard],
    component:LayoutComponent,
    children: [
      {
        path: 'dashboard',
        component:DashboardComponent
      },
      {
        path: 'wallet',
        component:WalletComponent
      },
      {
        path: 'bank-account',
        component:BankAccountComponent
      },
      {
        path: 'beneficiary',
        component: BeneficiaryComponent
      },
      {
        path: 'bill-payment',
        component:BillPaymentComponent
      },
      {
        path: 'transactions',
        component: TransactionComponent
      }
    ]
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    component:AdminLayoutComponent,
    children: [
      {
        path: '',
        component: AdminOverviewComponent
      },
      {
        path: 'customers',
        component: AdminCustomersComponent
      },
      {
        path: 'wallets',
        component: AdminWalletsComponent
      },
      {
        path: 'bank-accounts',
        component:AdminBankAccountsComponent
      },
      {
        path: 'beneficiaries',
        component: AdminBeneficiariesComponent
      },
      {
        path: 'bill-payments',
        component: AdminBillPaymentsComponent
      },
      {
        path: 'transactions',
        component: AdminTransactionsComponent
      }
    ]
  },

  { path: '**', redirectTo: '' }
];
