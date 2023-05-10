
import LoginPage from './modules/LoginPage';
import NotFoundPage from './modules/NotFoundPage';
import { Routes , Route } from 'react-router-dom'; 
import RegistrationPage from './modules/RegistrationPage';
import MainPage from './modules/MainPage';
import TransferAdminPage from './modules/admin/TransferAdminPage';
import TransferPage from "./modules/transfer/TransferPage";
import React from "react";
import DebitCardPage from "./modules/debit_card/DebitCardPage";
import CreditCardPage from './modules/credit_card/CreditCardPage';
import CreditCardAdminPage from './modules/admin/CreditCardAdminPage';
import CreditPage from './modules/credit/CreditPage';
import CreditMainAdminPage from './modules/admin/CreditMainAdminPage';
import PenniCreditCardAdminPage from './modules/admin/PenniCreditCardAdminPage';
import CreateRateAdminPage from './modules/admin/CreateRateAdminPage';
import RateTableAdminPage from './modules/admin/RateTableAdminPage';
import EditRateAdminPage from './modules/admin/EditRateAdminPage';
import RatePage from './modules/deposit/RatePage';
import DepositPage from './modules/deposit/DepositPage';


function App() {

  return (
    <Routes>
      <Route path="/registration" element={<RegistrationPage/>} />
      <Route path="/login" element={<LoginPage/>} />
      <Route path="/" element={<MainPage/>} />
      <Route path="/cards/debit/:id" element={<DebitCardPage id=":id"/>} />
      <Route path="/cards/credit/:id" element={<CreditCardPage id=":id"/>} />
      <Route path="/transfer" element={<TransferPage/>} />
      <Route path="/credits" element={<CreditPage/>} />
      <Route path="/admin/transfer" element={<TransferAdminPage/>} />
      <Route path="/admin/credits" element={<CreditMainAdminPage/>} />
      <Route path="/cards/credit/admin/:id" element={<CreditCardAdminPage id=":id"/>} />
      <Route path="/cards/credit/admin/penni/:id" element={<PenniCreditCardAdminPage id=":id"/>} />
      <Route path="/rate/create" element={<CreateRateAdminPage/>} />
      <Route path="/admin/rates" element={<RateTableAdminPage/>} />
      <Route path="/admin/rates/:id" element={<EditRateAdminPage id={":id"}/>} />
      <Route path="/rate" element={<RatePage/>} />
      <Route path="/deposit/:id" element={<DepositPage id={":id"}/>} />
      <Route path="*" element={<NotFoundPage/>} />
    </Routes>
  );
}

export default App;
