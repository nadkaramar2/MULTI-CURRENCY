import React, { useEffect, useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";

import "react-tabs/style/react-tabs.css";
import CustomAlert from "../../layout/CustomAlert";

export default function Gl_to_GlTransfer() {
  const [amount, setAmount] = useState("");
  const [narration, setNarration] = useState("");
  // From Account
  const [flist, setFlist] = useState([]);
  const [ftype, setFtype] = useState("");
  const [fNameCust, setFNameCust] = useState([]);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // To Account
  const [totype, setTotype] = useState("");
  const [tolist, setTolist] = useState([]);
  const [toNameCust, setToNameCust] = useState([]);

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

  useEffect(() => {
    fromtype();
  }, []);

  const fromtype = async () => {
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
        setFlist(response.data.glAccountviewModels);
      } else {
        // showError(response.data);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // cust id and holder name

  const GLDescription = fNameCust.map((t) => [t.strGLAccountDescription]);
  sessionStorage.setItem("GLDescription", GLDescription);
  let GLdescriptionfrom = sessionStorage.getItem("GLDescription");

  const GlAccountNumber = fNameCust.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("GlAccountNumber", GlAccountNumber);
  let AccountNumberfrom = sessionStorage.getItem("GlAccountNumber");

  const ClosingBalance = fNameCust.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("ClosingBalance", ClosingBalance);
  let closingBalancefrom = sessionStorage.getItem("ClosingBalance");
  useEffect(() => {
    NameCust();
    setError("");
  }, [ftype]);
  const NameCust = async () => {
    if (ftype === "" || ftype.length === 0) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`gl-account-type/getGLDescrption`, {
          strGLAccountType: ftype,
        });
        if (response.data.code === "S0000") {
          setFNameCust(response.data.glAccountviewModels);

          // showSuccess(response.data.message);
        } else {
          // showError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.);
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
      // handleShowError(error);
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

  let userId = localStorage.getItem("userName");

  useEffect(() => {
    NameCust1();
    setError("");
  }, [totype]);
  const NameCust1 = async () => {
    if (totype === "" || totype.length === 0) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`gl-account-type/getGLDescrption`, {
          strGLAccountType: totype,
        });
        if (response.data.code === "S0000") {
          setToNameCust(response.data.glAccountviewModels);

          // swal(response.data.message);
        } else {
          // handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error);
      }
    }
  };

  // Save data  form

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (
      totype === "" ||
      totype.length === 0 ||
      ftype === "" ||
      amount === "" ||
      narration === ""
    ) {
      setError(true);
    } else if (!userId) {
      handleShowError("Please check your userId");
    } else if (!AccountNumberfrom || !AccountNumberTo) {
      handleShowError("Please check your From AccountNumber ");
    } else if (!AccountNumberTo) {
      handleShowError("Please check your To Account Number ");
    } else {
      try {
        const response = await amsApi.post(
          `journalTransfer/addJournalTransferEntry`,
          {
            strFromAccountType: ftype,
            strFromAccountNumber: AccountNumberfrom,
            strToAccountType: totype,
            strToAccountNumber: AccountNumberTo,
            strAmoutToTransfer: amount,
            strNarration: narration,
            txnJournalTransferType: "G2G",
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
        handleShowError(error);
      }
    }
  };

  // Clear data
  const handleClick = () => {
    setAmount("");
    setNarration("");
    setFtype("");
    setTotype("");
    setFNameCust([]); // Clear from Account-related data
    setToNameCust([]);
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base mb-1 sm:text-xm text-black :text-2xl  leading-normal ">
                GL To GL Transfer
              </p>
            </div>
          </div>
        </div>

        <div className="overflow-hidden shadow mt-1">
          <form className="" onSubmit={handleSubmit}>
            <div className="grid  md:grid-cols-2 ">
              <div className=" px-4 sm:px-10 bg-rose-300  ">
                <div className="grid gap-4  md:grid-cols-2  ">
                  <div>
                    <label
                      htmlFor="GLAccountType"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From GL Account Type{""}
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
                        <option className="capatlize text-lg">
                          {data.strGLAccountType}
                        </option>
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
                  <div className=" text-start ">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From GL Account Description
                      <span className="text-red-600 px-2">*</span>
                    </label>

                    <input
                      type="text"
                      id="amount"
                      // onClick={NameCust}
                      autoComplete="off"
                      disabled
                      defaultValue={GLdescriptionfrom}
                      placeholder="Enter Gl Account Desc.."
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                  <div className="text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From GL Account Number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="amount"
                      defaultValue={AccountNumberfrom}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter GL account number"
                    />
                  </div>
                  <div className=" text-start">
                    {""}
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      From GL Account Balance
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="amount"
                      disabled
                      defaultValue={closingBalancefrom}
                      placeholder="Enter Gl Account Balance"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                </div>
              </div>

              {/* To Account Form */}
              <div className=" px-4 py-1 bg-teal-300">
                <div className="grid gap-6  md:grid-cols-2  ">
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
                  <div className="text-start">
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
                      id="amount"
                      autoComplete="off"
                      disabled
                      placeholder="Enter To GL Account Desc.."
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
                      id="amount"
                      disabled
                      defaultValue={AccountNumberTo}
                      placeholder="Enter To GL Account Number"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="overflow-hidden shadow ">
              <div className=" px-4 bg-white ">
                <div className="grid gap-6 md:grid-cols-2 p-2 ">
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
                          setErrorMessage(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (amount.length === 0) {
                          setError("Please Enter Amount!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Amount to Transfer"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && amount.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Amount!
                      </p>
                    ) : null}
                    {/* {error && amount.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please Enter Amount
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Narration <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="narration"
                      value={narration}
                      onChange={(e) => setNarration(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Narration "
                      autoComplete="off"
                    />
                    {error && narration.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Narration
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

      {/*  */}
    </AppLayout>
  );
}
