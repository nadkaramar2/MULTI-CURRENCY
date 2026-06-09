import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Sidebar from "../components/sidebar";
import { useSelector } from "react-redux";
import Navbar from "../components/navbar";
import Dashboard from "../components/dashboard/Dashboard";
import ViewMcc from "../components/configuration/ViewMcc";
import CreateMcc from "../components/configuration/CreateMcc";

// CONFIGURATION
import CreateViewControlAccount from "../components/configuration/CreateViewControlAccount";
import NubanTypeConfig from "../components/configuration/NubanTypeConfig";
import ControlAccount from "../components/configuration/ControlAccount";
import GLaccountCreation from "../components/configuration/GLaccountCreation";
import ViewGLaccount from "../components/configuration/ViewGLaccount";
import CreateAccountType from "../components/configuration/CreateAccountType";
import AccountConfiguration from "../components/configuration/AccountConfiguration";
import CreateCreditLimitCategory from "../components/configuration/CreateCreditLimitCategory";
import CreateTransactionType from "../components/configuration/CreateTransactionType";
import EditAccountType from "../components/configuration/EditAccountType";
import VatTypeConfiguration from "../components/configuration/VatTypeConfiguration";
import FeeTypeconfiguration from "../components/configuration/FeeTypeconfiguration";
import ViewTransactionType from "../components/configuration/ViewTransactionType";
import CreateCategory from "../components/configuration/CreateCategory";
import ViewCategory from "../components/configuration/ViewCategory";
import CreateChannel from "../components/configuration/CreateChannel";
import ViewChannel from "../components/configuration/ViewChannel";
import MiddlwareAppInfo from "../components/configuration/MiddlwareAppInfo";
import CreatePoolAccountMaster from "../components/configuration/CreatePoolAccountMaster";
import ViewDormantA from "../dormancy/ViewDormantA";
import ResponseCodeDescrition from "../components/configuration/ResponseCodeDescription";
import ViewAuthorizeDormantList from "../components/configuration/ViewAuthorizeDormantList";

// poolaccountmaster;
// MULTI WALLET CONFIGURATION
import ParticipantWise from "../components/walletconfig/ParticipantWise";
import MccConfigForm from "../components/walletconfig/MccConfigForm";
import AccountTypeWise from "../components/walletconfig/AccountTypeWise";
// CHARGING MODULE
import AddChargerelated from "../components/chargingmodule/AddChargerelated";
import ChargeType from "../components/chargingmodule/ChargeType";
import ChargingConfigu from "../components/chargingmodule/ChargingConfigu";
// CREDIT CARD CONFIGURATION
import MccWiseInterest from "../components/cardcalculation/MccWiseInterest";
import ViewMccInterest from "../components/cardcalculation/ViewMccInterest";
import RevolvingCredit from "../components/cardcalculation/RevolvingCredit";
// REPORTS
import AccountStatement from "../components/reports/AccountStatement";
import Chart_Accounts from "../components/reports/Chart_Accounts";
import GLaccountstmt from "../components/reports/GLaccountstmt";
import GlAccountsub from "../components/reports/GlAccountsub";
import TxnReport from "../components/reports/TxnReport";
import JournalTxnReport from "../components/reports/JournalTxnReport";
import RegisteredCus from "../components/reports/RegisteredCus";
import RegistedLinkedaccount from "../components/reports/RegistedLinkedaccount";
import Cashwithdrawal from "../components/reports/Cashwithdrawal";
import CashDeposite from "../components/reports/CashDeposite";
import FeesCollected from "../components/reports/FeesCollected";
import ViewAccountBalance from "../components/reports/ViewAccountBalance";
import VatCollected from "../components/reports/VatCollected";
import TransactionRequestResponse from "../components/reports/TransactionRequestResponse";
// import responseTransactionReports from "../components/reports/"
import AccountSummery from "../components/reports/AccountSummery.jsx";
import ViewUnbilledTxn from "../components/reports/ViewUnbilledTxn.jsx";
import ViewBilledTxn from "../components/reports/ViewBilledTxn.jsx";
import VieeInterestTxn from "../components/reports/ViewIntrestTxn.jsx";
//Reward
import CashBackmaster from "../components/reward/CashTypeMaster";
import RewardMaster from "../components/reward/RewardTypeMaster";
import ViewReaardType from "../components/reward/ViewRewardType";
import ViewCashBackType from "../components/reward/ViewCashBackType";
// LOAD BALANCE
import NonCreditLoadblc from "../components/loadbalance/NonCreditLoadblc";
import GLAccountLoadblc from "../components/loadbalance/GLAccountLoadblc";
// JOURNAL TRANSFER
import GltoGlTransfer from "../components/journaltransfer/GltoGlTransfer";
import GLtoAccount from "../components/journaltransfer/GLtoAccount";
import AccountToGL from "../components/journaltransfer/AccountToGL";
import AccountToAccount from "../components/journaltransfer/AccountToAccount";
// TRANSFER
import TransferInOut from "../components/transfer/TransferInOut";
// CUSTOMER ACCOUNT
import CreateCustomer from "../components/customerAccount/CreateCustomer";
import ViewEditCustomer from "../components/customerAccount/ViewEditCustomer";
import EditCustomer from "../components/customerAccount/EditCustomer";
import ApproveUpgradeTier from "../components/customerAccount/ApproveUpgradeTier";
import UpgradeCustTier from "../components/customerAccount/UpgradeCustTier";
import ViewEditAccount from "../components/customerAccount/ViewEditAccount";
import CreateInstantAccount from "../components/customerAccount/CreateInstantAccount";
import EditAccount from "../components/customerAccount/EditAccount";
import ViewEditInstantAccount from "../components/customerAccount/ViewEditInstantAccount";
import ApproveAccount from "../components/customerAccount/ApproveAccount";
import ApproveLinkAccount from "../components/customerAccount/ApproveLinkAccount";
import EditInstantAccount from "../components/customerAccount/EditInstantAccount";
// LINK CARD
import CardAccountLinkage from "../components/linkCard/CardAccountLinkage";
import ViewLinkedCard from "../components/linkCard/ViewLinkedCard";
// SEARCH TXN
import SearchTransaction from "../components/searchTransaction/SearchTransaction";
// Authorize Journal Transfer
import AuthorizeJournalTransfer from "../components/athorizeJournalTransfer/AuthorizeJournalTransfer";
// Search Customer
import SearchCustomer from "../components/customerSearch/SearchCustomer";
import AccountDetails from "../components/customerSearch/AccountDetails";
import CardDetails from "../components/customerSearch/CardDetails";
import AccountStatemet from "../components/customerSearch/AccountStatemet";

// Approve Reject Journal Transfer
import ApproveRejectJournalTransfer from "../components/athorizeJournalTransfer/AproovRejectJournalTransfer";
// ROLE BASED
import CreateUser from "../components/rollBased/CreateUser";
import ViewUser from "../components/rollBased/ViewUser";
import CreateParticipant from "../components/rollBased/CreateParticipant";
import MenuList from "../components/rollBased/MenuList";
import AddRole from "../components/rollBased/AddRole";
// CLOSER ACCOUNT
import ClosureAccountList from "../components/closureAccount/CloserAccountList";
import CloseAccpountForm from "../components/closureAccount/CloseAccpountForm";
import AccountClosureRequest from "../components/closureAccount/AccountClosureRequest";
// BULK TRANSFER
import OneToMany from "../components/bulkTransfer/OneToMany";
import ManyOne from "../components/bulkTransfer/ManyOne";
import GLtoMany from "../components/bulkTransfer/GLtoMany";
import ManyToGL from "../components/bulkTransfer/ManyToGL";
import BulkTransfer from "../components/bulkTransfer/BulkTransfer";
import Verify from "../components/bulkTransfer/Verify";
import Verifylist from "../components/bulkTransfer/Verifylist";
// import EditAccountType from "../components/bulkTransfer/EditAccountType";
import Authorize from "../components/bulkTransfer/Authorize";
import AuthorizeApprove from "../components/bulkTransfer/AuthorizeApprove";
import CreateAccount from "../components/customerAccount/CreateAccount";
// SYSTEM TALLY
import AccountAndGLSystemTally from "../components/systemtally/AccountAndGlTally";
import ControlAccountTally from "../components/systemtally/ControlAccountTally";
// CURRENCY MANAGEMENT
import CreateCurrency from "../components/currencyManagement/CreateCurrency";
import ViewCurrency from "../components/currencyManagement/ViewCurrency";
import EditCurrency from "../components/currencyManagement/EditCurrency";
import NetworkTypeMaster from "../components/currencyManagement/NetworkTypeMaster";
import AddChargeType from "../components/currencyManagement/ChargeTypeMaster";
import GSTTypeMaster from "../components/currencyManagement/GSTTypeMaster.jsx";
import ViewCurrencyTransferMaster from "../components/currencyManagement/ViewCurrencyTransferMaster.jsx";

// MultiCurrency Report
import MultiCurrencyLoadMoneyReport from "../components/multiCurrencyReport/MultiLoadMoneyReport.jsx";
import MultiCurrencyfincialYearReport from "../components/multiCurrencyReport/MulticurrencyFincialYearReport.jsx";
import Currencyfeereports from "../components/reports/Currencyfeereports.jsx";
import Currencygstreports from "../components/reports/Currencygstreports.jsx";
import ViewMultiCurrencyCharges from "../components/multiCurrencyReport/ViewMultiCurrencyReport.jsx";
import MultiCurrencyChargesType from "../components/multiCurrencyReport/AddChargesType.jsx";

// Dormancy
// import DormantTable from "../components/dormancy/DormantTable";
// import AuthorizeDormancy from "../components/dormancy/AuthorizeDormancy";
// import AuthorizeView from "../components/dormancy/AuthorizeView";
// import Daccountnumber from "../components/dormancy/Daccountnumber";

// LOGIN
import Login from "../auth/Login";
import DormantTable from "../dormancy/DormantTable";
import Daccountnumber from "../dormancy/Daccountnumber";
import AuthorizeDormancy from "../dormancy/AuthorizeDormancy";
import AuthorizeView from "../dormancy/AuthorizeView";
import Cashback_Statement from "../components/reports/Cashback_Statement";
import RewardStatements from "../components/reports/RewardStatements";
import ForgotPassword from "../auth/ForgotPassword";
import Transactiondetails from "../components/reports/Transactiondetails";
import SetalledMerchantTxnStatement from "../components/reports/SetalledMerchantTxnStatement";
import ViewMerchantTxnStatement from "../components/reports/ViewMerchantTxnStatement.jsx";
import Rescodemaster from "../components/configuration/Rescodemaster.jsx";
import Response from "../components/reports/Response.jsx";
import Addtcstype from "../components/currencyManagement/Addtcstype.jsx";
import Currencyconversion from "../components/currencyManagement/Currencyconversion.jsx";
import MultiCurrencyFeetype from "../components/currencyManagement/MultiCurrencyFeetype.jsx";
import CountTypeLinkedd1 from "../components/currencyManagement/CountTypeLinkedGl.jsx";
import ViewWalletAccountMaster from "../components/currencyManagement/ViewWalletAccountMaster.jsx";
import MulticurrencyCharge from "../components/multiCurrencyReport/MulticurrencyCharge.jsx";
import ViewBillingcycle from "../components/configuration/ViewBillingcycle.jsx";
import Viewrevolvingcreditcard from "../components/configuration/Viewrevolvingcreditcard.jsx";
import ViewCreditcardmaster from "../components/configuration/ViewCreditcardmaster.jsx";
// import CreditCardCongigurationGL from "../components/cardcalculation/CreditCardconfigurationGL.jsx";
import CreditCardconfigurationGL from "../components/cardcalculation/CreditCardconfigurationGL.jsx";
import ViewAccountCatogoryLimit from "../components/configuration/ViewAccountCatogoryLimit .jsx";

const MainRoutes = () => {
  const { authInfo } = useSelector((state) => state.auth);
  let allowedRoutes = [];
  return (
    <>
      {authInfo !== null && <Navbar />}
      {authInfo !== null && (
        <div className="flex fixed top-12 scroll-m-0  w-full">
          <Sidebar routes={allowedRoutes} />
          <div className="h-screen flex-1 w-full">
            <Routes>
              <Route
                path="/"
                element={
                  authInfo === null ? (
                    <Navigate to="/ams/login" replace={true} />
                  ) : (
                    <Navigate to="/dashboard" replace={true} />
                  )
                }
              />
              {/* DASHBOARD */}
              {/* <Route path="/dashboard" element={<Dashboard />} /> */}
              {/* Dornmant Creation */}
              <Route path="/dormanttable" element={<DormantTable />} />
              <Route path="/dormanttable" element={<DormantTable />} />
              <Route path="/convertdormancy" element={<Daccountnumber />} />
              <Route
                path="/authorise dormant"
                element={<AuthorizeDormancy />}
              />
              <Route
                path="/authorizeview/:accountNumber"
                element={<AuthorizeView />}
              />
              {/* ROLE BASED */}
              <Route path="/create-user" element={<CreateUser />} />
              <Route path="/view-user" element={<ViewUser />} />
              <Route
                path="/create-participant"
                element={<CreateParticipant />}
              />
              <Route path="/menulist" element={<MenuList />} />
              <Route path="/role" element={<AddRole />} />
              {/* CONFIGURATION */}
              <Route
                path="/createview"
                element={<CreateViewControlAccount />}
              />
              <Route path="/nuban_type_Config" element={<NubanTypeConfig />} />
              <Route
                path="/create-account-category"
                element={<ControlAccount />}
              />
              <Route
                path="/gl-account-creation"
                element={<GLaccountCreation />}
              />
              <Route
                path="/view-gl-accountcreation"
                element={<ViewGLaccount />}
              />
              <Route
                path="/create-account-type"
                element={<CreateAccountType />}
              />
              <Route
                path="/view-account-type"
                element={<AccountConfiguration />}
              />
              <Route
                path="/create-credit-limit-category"
                element={<CreateCreditLimitCategory />}
              />

              <Route
                path="/viewCreditLimit"
                element={<ViewAccountCatogoryLimit />}
              />
              <Route path="/txn-type" element={<CreateTransactionType />} />
              <Route
                path="/vat-type-configuration"
                element={<VatTypeConfiguration />}
              />
              <Route
                path="/fee-type-configuration"
                element={<FeeTypeconfiguration />}
              />
              <Route
                path="/editaccount/:strAccountType"
                element={<EditAccountType />}
              />
              <Route
                path="/view-transaction-type"
                element={<ViewTransactionType />}
              />
              <Route path="/createcategory" element={<CreateCategory />} />
              <Route path="/viewcategory" element={<ViewCategory />} />
              <Route path="/createchannel" element={<CreateChannel />} />
              <Route path="/viewchannel" element={<ViewChannel />} />
              <Route path="/middlewareInfo" element={<MiddlwareAppInfo />} />
              <Route
                path="/poolaccountmaster"
                element={<CreatePoolAccountMaster />}
              />
              <Route path="/addmcc" element={<CreateMcc />} />
              <Route path="/viewmcc" element={<ViewMcc />} />
              <Route path="/viewDormantAccounts" element={<ViewDormantA />} />
              <Route path="/responseCodeMaster" element={<Rescodemaster />} />
              <Route
                path="/responseDescription"
                element={<ResponseCodeDescrition />}
              />
              <Route
                path="/viewDormantAuthorize"
                element={<ViewAuthorizeDormantList />}
              />
              <Route path="/viewbillingcycle" element={<ViewBillingcycle />} />
              <Route
                path="/viewrevolvingcreditcard"
                element={<Viewrevolvingcreditcard />}
              />
              <Route
                path="/viewCreditcardmaster"
                element={<ViewCreditcardmaster />}
              />
              {/* MULTI WALLET CONFIGURATION */}
              <Route path="/parti-wise" element={<ParticipantWise />} />
              <Route
                path="/block-mcc config form"
                element={<MccConfigForm />}
              />
              <Route path="/account-type-wise" element={<AccountTypeWise />} />
              {/* CHARGING MODULE */}
              <Route path="/add-chargerelated" element={<AddChargerelated />} />
              <Route path="/charge-type" element={<ChargeType />} />
              <Route path="/charging-config" element={<ChargingConfigu />} />
              {/* Credit Card Config */}
              <Route path="/mccwise-interest" element={<MccWiseInterest />} />
              <Route path="/view-mcc-interest" element={<ViewMccInterest />} />
              <Route path="/revolving-credit" element={<RevolvingCredit />} />
              <Route
                path="/creditCardConfrigationGl"
                element={<CreditCardconfigurationGL />}
              />

              {/* REPORTS */}
              <Route
                path="/account-statements"
                element={<AccountStatement />}
              />
              <Route path="/chart-accounts" element={<Chart_Accounts />} />
              <Route path="/glaccountstmt" element={<GLaccountstmt />} />
              <Route path="/feescollected" element={<FeesCollected />} />
              <Route
                path="/viewaccountbalance"
                element={<ViewAccountBalance />}
              />
              <Route path="/vatcollected" element={<VatCollected />} />
              <Route
                path="/GlAccountsub/:strGLAccountType/:strAccountNumber"
                element={<GlAccountsub />}
              />
              <Route path="/txn-report" element={<TxnReport />} />
              <Route
                path="/transactiondetails/:strTxn_id"
                element={<Transactiondetails />}
              />
              <Route
                path="/journal-txn-report"
                element={<JournalTxnReport />}
              />
              <Route path="/registered-cus" element={<RegisteredCus />} />
              <Route
                path="/registed-linked-account"
                element={<RegistedLinkedaccount />}
              />
              <Route path="/cash-withdrawal" element={<Cashwithdrawal />} />
              <Route path="/cash-deposite" element={<CashDeposite />} />
              <Route
                path="/txnReqResp"
                element={<TransactionRequestResponse />}
              />
              <Route path="/txnReqResp" element={<AccountSummery />} />
              <Route
                path="/responseTransactionReports"
                element={<Response />}
              />
              <Route
                path="Cashback Statment"
                element={<Cashback_Statement />}
              />
              <Route
                path="Reward Points Statment"
                element={<RewardStatements />}
              />
              <Route
                path="/merchantSettledStatement"
                element={<SetalledMerchantTxnStatement />}
              />
              <Route
                path="/merchantTxnStatement"
                element={<ViewMerchantTxnStatement />}
              />
              <Route
                path="/multicurrencyfeereports"
                element={<Currencyfeereports />}
              />
              <Route
                path="/multicurrencygstreports"
                element={<Currencygstreports />}
              />
              <Route
                path="/multicurrencychargesreport"
                element={<MulticurrencyCharge />}
              />
              <Route path="/viewUnbilledTxn" element={<ViewUnbilledTxn />} />
              <Route
                path="/viewBilledTransaction"
                element={<ViewBilledTxn />}
              />
              <Route
                path="/viewInterestTrnsaction"
                element={<VieeInterestTxn />}
              />
              {/* LOAD BALANCE */}
              <Route
                path="/non-credit-loadblc"
                element={<NonCreditLoadblc />}
              />
              <Route
                path="/gl-account-loadblc"
                element={<GLAccountLoadblc />}
              />
              {/* JOURNAL TRANSFER */}
              <Route path="/gl-to-gl" element={<GltoGlTransfer />} />
              <Route path="/gl-to-account" element={<GLtoAccount />} />
              <Route path="/account-to-gl" element={<AccountToGL />} />
              <Route
                path="/account-to-account"
                element={<AccountToAccount />}
              />
              {/* Reward */}
              <Route path="/cashbackmaster" element={<CashBackmaster />} />
              <Route path="/rewardmaster" element={<RewardMaster />} />
              <Route
                path="/cashbackTypeMasterView"
                element={<ViewCashBackType />}
              />
              <Route
                path="/rewardPointMasterView"
                element={<ViewReaardType />}
              />
              {/* TRANSFER */}
              <Route path="/transfer-in-out" element={<TransferInOut />} />
              {/* CUSTOMER ACCOUNT */}
              <Route path="/create-customer" element={<CreateCustomer />} />
              <Route
                path="/view-edit-customer"
                element={<ViewEditCustomer />}
              />
              <Route
                path="/edit-customer/:strCustId"
                element={<EditCustomer />}
              />
              <Route path="/approve-upgrade" element={<ApproveUpgradeTier />} />
              <Route path="/create-account" element={<CreateAccount />} />
              <Route path="/view-edit-account" element={<ViewEditAccount />} />
              <Route
                path="/editinstantaccount/:strAccountType"
                element={<EditInstantAccount />}
              />
              <Route
                path="/upgrade-custtier/:strCustId/:strTierType"
                element={<UpgradeCustTier />}
              />
              <Route
                path="/create-instant-account"
                element={<CreateInstantAccount />}
              />
              <Route
                path="/edit-account/:strAccountType/:strAccountNumber"
                element={<EditAccount />}
              />
              <Route
                path="/view-edit-instant-account"
                element={<ViewEditInstantAccount />}
              />
              <Route
                path="/approve_link_account/:strMobileNo"
                element={<ApproveLinkAccount />}
              />
              <Route path="/approve_account" element={<ApproveAccount />} />
              <Route
                path="/approve_link_account/:strMobileNo"
                element={<ApproveLinkAccount />}
              />
              {/* LINK CARD */}
              <Route
                path="/Cardaccountlinkage"
                element={<CardAccountLinkage />}
              />
              <Route path="/viewlinkedcards" element={<ViewLinkedCard />} />
              {/* SEARCH TRANSACTION */}
              <Route
                path="/SearchTransaction"
                element={<SearchTransaction />}
              />
              {/* Authorize Journal Transfer */}
              <Route
                path="/authorize-journal-transfer"
                element={<AuthorizeJournalTransfer />}
              />
              {/* SEARCH CUSTOMER */}
              <Route path="/customer-search" element={<SearchCustomer />} />
              <Route path="/account-details" element={<AccountDetails />} />
              <Route path="/card-details" element={<CardDetails />} />
              <Route
                path="/account-statement/:fromdate/:todate/:selectedRowData"
                element={<AccountStatemet />}
              />
              {/* <Route
          path="/account-statement/:fromdate/:todate/:selectedRowData"
          element={<AccountStatemet />}
        /> */}
              {/* APPROVE REJECT JOURNAL TRANSFER */}
              <Route
                path="/approve-journal-transfer/:strTxnId"
                element={<ApproveRejectJournalTransfer />}
              />
              {/* Closer Account */}
              <Route
                path="/Close-Account-Checker"
                element={<ClosureAccountList />}
              />
              <Route path="/close-account" element={<CloseAccpountForm />} />
              <Route
                path="/accountcloserequest"
                element={<AccountClosureRequest />}
              />
              {/* BULK TRANSFER  */}
              <Route path="/one-to-many" element={<OneToMany />} />
              <Route path="/many-to-one" element={<ManyOne />} />
              <Route path="/gl-to-many" element={<GLtoMany />} />
              <Route path="/many-to-gl" element={<ManyToGL />} />
              <Route path="/upload" element={<BulkTransfer />} />
              <Route path="/verify" element={<Verify />} />
              <Route
                path="/verifylist/:strBulkMode/:strPreTransactionId"
                element={<Verifylist />}
              />
              <Route path="authorize" element={<Authorize />} />
              <Route
                path="authorizeapprove/:strBulkMode/:strPreTransactionId"
                element={<AuthorizeApprove />}
              />
              {/* SYSTEM TALLY */}
              <Route
                path="systemtally/accountgls"
                element={<AccountAndGLSystemTally />}
              />
              <Route
                path="systemtally/controlaccount"
                element={<ControlAccountTally />}
              />
              <Route path="/ams/login" element={<Login />} />
              {/* MultiCurrency Report */}
              <Route
                path="/multicurrencyloadmoneyreports"
                element={<MultiCurrencyLoadMoneyReport />}
              />
              <Route
                path="/multicurrencyfinancialyearreports"
                element={<MultiCurrencyfincialYearReport />}
              />
              <Route
                path="/multicurrencychargeview"
                element={<ViewMultiCurrencyCharges />}
              />
              <Route
                path="/multicurrencychargestype"
                element={<MultiCurrencyChargesType />}
              />
              {/*Currency Management */}
              <Route path="/addcurrency" element={<CreateCurrency />} />
              <Route path="/viewcurrency" element={<ViewCurrency />} />
              <Route path="/editcurrency/:country" element={<EditCurrency />} />
              <Route path="/addNetworkType" element={<NetworkTypeMaster />} />
              <Route path="/addChargeType" element={<AddChargeType />} />
              <Route path="/gstType" element={<GSTTypeMaster />} />
              <Route
                path="/viewCurrencyTransferMaster"
                element={<ViewCurrencyTransferMaster />}
              />
              <Route
                path="/currecncyConversion"
                element={<Currencyconversion />}
              />
              <Route
                path="/multiCurrencyFeeTypeMaster"
                element={<MultiCurrencyFeetype />}
              />
              <Route path="/addTcsType" element={<Addtcstype />} />
              <Route
                path="/multicurrencyAccountTypeLinkedGl"
                element={<CountTypeLinkedd1 />}
              />
              <Route
                path="/viewmulticurrencyAccountTypeLinkedGl"
                element={<ViewWalletAccountMaster />}
              />
            </Routes>
          </div>
        </div>
      )}
      <Routes>
        <Route
          path="/"
          element={
            authInfo === null ? (
              <Navigate to="/ams/login" replace={true} />
            ) : (
              <Navigate to="/dashboard" replace={true} />
            )
          }
        />
        {/* {/* DASHBOARD  */}
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/ams/login" element={<Login />} />
        <Route path="/password" element={<ForgotPassword />} />
      </Routes>
    </>
  );
};

export default MainRoutes;
