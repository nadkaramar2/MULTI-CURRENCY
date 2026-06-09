import React, { useEffect, useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import "react-tabs/style/react-tabs.css";
import CustomAlert from "../../layout/CustomAlert";
export default function AccountToGL() {
  const [amount, setAmount] = useState("");
  const [narration, setNarration] = useState("");

  // From Account

  const [fromdata, setFromdata] = useState([]);
  const [flist, setFlist] = useState([]);
  const [ftype, setFtype] = useState("");
  const [faccountno, setFAccountno] = useState("");
  const [fNameCust, setFNameCust] = useState([]);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");

  // to Account
  const [totype, setTotype] = useState("");
  const [tolist, setTolist] = useState([]);
  const [toNameCust, setToNameCust] = useState([]);

  // From GL to Account

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const handleClick = () => {
    setAmount("");
    setNarration("");
    setFtype("");
    setFAccountno("");
    setTotype("");
    setFromdata([]);
    setFNameCust([]);
    setToNameCust([]);
  };

  // to dropdown

  useEffect(() => {
    fromtype();
  }, []);

  const fromtype = async () => {
    try {
      const response = await amsApi.get(
        `accountType/getAccntType`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setFlist(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data);
      }
    } catch (error) {
      // showError(error);
    }
  };

  // fromdesription

  const Description = fromdata.map((t) => [t.strDescription]);
  sessionStorage.setItem("Description", Description);
  let Description1 = sessionStorage.getItem("Description");
  useEffect(() => {
    description();
    setError("");
  }, [ftype]);
  const description = async () => {
    if (ftype === "" || ftype.length === 0) {
      // setError(true);
    } else {
      try {
        const response = await amsApi.post(`accountType/getAccDescrption`, {
          strAccountType: ftype,
        });
        if (response.data.code === "S0000") {
          setFromdata(response.data.accountTypeMasterlistData);

          // swal(response.data.message);
        } else {
          // showError(response.data.message);
        }
      } catch (error) {
        // showError(error);
      }
    }
  };

  // cust id and holder name

  const CustId = fNameCust.map((t) => [t.strCustId]);
  sessionStorage.setItem("CustId", CustId);
  let custId = sessionStorage.getItem("CustId");

  const AccountHolderName = fNameCust.map((t) => [t.strAccountHolderName]);
  sessionStorage.setItem("AccountHolderName", AccountHolderName);
  let accountHolderName = sessionStorage.getItem("AccountHolderName");

  const ClosingBalance = fNameCust.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("ClosingBalance", ClosingBalance);
  let closingBalance = sessionStorage.getItem("ClosingBalance");
  const NameCust = async () => {
    if (
      ftype === "" ||
      ftype.length === 0 ||
      faccountno === "" ||
      faccountno === 0
    ) {
      // setError(true);
    } else {
      try {
        const response = await amsApi.post(`account/getAccountBalancAndName`, {
          strAccountType: ftype,
          strAccountNumber: faccountno,
        });
        if (response.data.code === "S0000") {
          setFNameCust(response.data.accountInfoList);

          // swal(response.data.message);
        } else {
          // showError(response.data.message);
        }
      } catch (error) {
        // showError(error);
      }
    }
  };

  // to dropdown

  useEffect(() => {
    Totype();
  }, []);

  const Totype = async () => {
    try {
      const response = await amsApi.get(
        `gl-account-type/getThirdPartyAllowGLAcountTypeList`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setTolist(response.data.glAccountviewModels);
      } else {
        // showError(response.data);
      }
    } catch (error) {
      // showError(error);
    }
  };

  // cust id and holder name

  const GLAccountDescription = toNameCust.map((t) => [
    t.strGLAccountDescription,
  ]);
  sessionStorage.setItem("GLAccountDescription", GLAccountDescription);
  let GLAccountDescriptionTo = sessionStorage.getItem("GLAccountDescription");

  const toAccountNumber = toNameCust.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("toAccountNumber", toAccountNumber);
  let AccountNumberTo = sessionStorage.getItem("toAccountNumber");
  useEffect(() => {
    NameCust1();
    setError("");
  }, [totype]);
  const NameCust1 = async () => {
    try {
      const response = await amsApi.post(`gl-account-type/getGLDescrption`, {
        strGLAccountType: totype,
      });
      if (response.data.code === "S0000") {
        setToNameCust(response.data.glAccountviewModels);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error);
    }
  };
  // Save data  form
  let userId = localStorage.getItem("userName");
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (
      totype === "" ||
      totype.length === 0 ||
      faccountno === "" ||
      faccountno === 0 ||
      ftype === "" ||
      amount === "" ||
      narration === ""
    ) {
      setError(true);
    } else if (!userId || !AccountNumberTo) {
      handleShowError("Please check your userId ");
    } else if (!AccountNumberTo) {
      handleShowError("Please check your To GL AccountNumber ");
    } else {
      try {
        const response = await amsApi.post(
          `journalTransfer/addJournalTransferEntry`,
          {
            strFromAccountType: ftype,
            strFromAccountNumber: faccountno,
            strToAccountType: totype,
            strToAccountNumber: AccountNumberTo,
            strAmoutToTransfer: amount,
            strNarration: narration,
            txnJournalTransferType: "A2G",
            strMakerId: userId,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Account To GL Transfer
              </p>
            </div>
          </div>
        </div>
        <div className="overflow-hidden shadowmt-2 mt-2">
          <form className="" onSubmit={handleSubmit}>
            <div className="grid md:grid-cols-2 ">
              {/* From Account Form */}

              <div className=" px-4 py-1 bg-rose-300  ">
                <div className="grid gap-3  md:grid-cols-2   ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="type"
                      id="type"
                      value={ftype}
                      onChange={(e) => setFtype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {flist.map((data) => (
                        <optgroup key={uuidv4()}>
                          <option className="capatlize text-lg">
                            {data.strAccountType}
                          </option>
                        </optgroup>
                      ))}
                    </select>
                    {error && ftype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please select Account type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="grid2-item text-start">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Account Description
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <div>
                      <input
                        type="text"
                        onClick={description}
                        defaultValue={Description1}
                        autoComplete="off"
                        placeholder="Enter From Account Desc.."
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                    </div>
                  </div>

                  <div className="grid2-item  text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Account Number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="faccountno"
                      value={faccountno}
                      // onChange={(e) => setFAccountno(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setFAccountno(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(" Only allow numeric digits");
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onKeyUp={NameCust}
                      onBlur={() => {
                        if (faccountno.length === 0) {
                          setError("Please Enter Account Number!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account number"
                      autoComplete="off"
                    />

                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && faccountno.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Account Number!
                      </p>
                    ) : null}
                  </div>

                  <div className="grid2-item  text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Account Holder Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="accountHolderName"
                      // onClick={NameCust}
                      defaultValue={accountHolderName}
                      // disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      autoComplete="off"
                      placeholder="Enter Account holder name"
                    />
                  </div>
                  <div className="grid2-item  text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Account Balance
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="closingBalance"
                      defaultValue={closingBalance}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      autoComplete="off"
                      placeholder="Enter From Account Balance"
                    />
                  </div>
                  <div className="grid2-item  text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From Cust Id
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="custId"
                      defaultValue={custId}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      autoComplete="off"
                      placeholder="Enter From Customer Id"
                    />
                  </div>
                </div>
              </div>

              {/* To */}
              <div className=" px-4 py-1 bg-teal-300  ">
                <div className="grid gap-6  md:grid-cols-2 ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      To GL Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="type"
                      id="type"
                      value={totype}
                      onChange={(e) => setTotype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {tolist.map((data) => (
                        <option className="capatlize text-lg">
                          {data.strGLAccountType}
                        </option>
                      ))}
                    </select>
                    {error && totype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please select Account type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      To GL Account Description
                      <span className="text-red-600 px-2">*</span>
                    </label>

                    <input
                      type="text"
                      // onClick={NameCust1}
                      defaultValue={GLAccountDescriptionTo}
                      id="GLAccountDescriptionTo"
                      autoComplete="off"
                      placeholder="Enter To Gl Account Desc.."
                      // disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>

                  <div className="grid2-item  text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      To GL Account Number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="AccountNumberTo"
                      defaultValue={AccountNumberTo}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      autoComplete="off"
                      placeholder="Enter To Account number"
                    />
                  </div>
                </div>
              </div>
            </div>

            <div className="overflow-hidden shadow ">
              <div className="px-4 py-1  bg-white  ">
                <div className="grid gap-6  md:grid-cols-2 p-2">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Amount To Transfer
                      <span className="text-red-600 px-2">*</span>
                    </label>

                    <input
                      type="text"
                      id="amount"
                      value={amount}
                      // onChange={(e) => setAmount(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setAmount(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage1(" Only allow numeric digits");
                        } else {
                          setErrorMessage1("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (amount.length === 0) {
                          setError("Please Enter amount!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Amount to Transfer"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && amount.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter amount!
                      </p>
                    ) : null}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Narration<span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="narration"
                      value={narration}
                      onChange={(e) => setNarration(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Narration"
                      autoComplete="off"
                    />
                    {error && narration.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter narration
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  onClick={handleClick}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
