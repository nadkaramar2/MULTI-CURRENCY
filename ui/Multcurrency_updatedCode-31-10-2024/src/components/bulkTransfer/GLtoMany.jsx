import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import swal from "sweetalert";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import "react-tabs/style/react-tabs.css";
import CustomAlert from "../../layout/CustomAlert";
export default function GLtoMany() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [totype, setToType] = useState([]);
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  let useid = localStorage.getItem("userName");

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

  // From Account type
  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
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
        setType(response.data.glAccountviewModels);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);

      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };

  // To account type
  useEffect(() => {
    toAccounttype();
  }, []);

  const toAccounttype = async () => {
    try {
      const response = await amsApi.get(`accountType/getAccntType`, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.data.code === "S0000") {
        setToType(response.data.accountTypeMasterlistData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);

      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };
  // Added new row input fileds
  const [isValidateButtonDisabled, setIsValidateButtonDisabled] =
    useState(false);
  const [isRowAdded, setIsRowAdded] = useState(true);
  const [inputFields, setInputFields] = useState([
    {
      strToAccountType: "",
      strToAccountNo: "",
      strTransactionAmount: "",
      holdername: "",
    },
  ]);

  // start
  const validateTransferAmount = (mainTransferAmount, inputFields) => {
    for (const field of inputFields) {
      const transferAmount = parseFloat(field.strTransactionAmount);
      if (transferAmount > mainTransferAmount) {
        swal(
          "Validation Error",
          "Transfer amount cannot be greater than the main transfer amount.",
          "error"
        );
        return false;
      }
    }
    return true;
  };

  const calculateTotalTransferAmount = (inputFields) => {
    let totalTransferAmount = 0;
    for (const field of inputFields) {
      totalTransferAmount += parseFloat(field.strTransactionAmount);
    }
    return totalTransferAmount;
  };

  const handleAddFields = () => {
    const lastRow = inputFields[inputFields.length - 1];
    const mainTransferAmount = parseFloat(amount);

    // Check if required fields for the "From Account Information" are empty
    if (accounttype === "" || mainTransferAmount <= 0) {
      swal(
        "Validation Error",
        "Please fill in all required fields (Account Type, Account Number, and Amount) in From Account Information.",
        "error"
      );
      return;
    }

    // Check if any of the "To Account Information" fields are empty in any row
    for (const field of inputFields) {
      if (
        field.strToAccountType === "" ||
        field.strToAccountNo === "" ||
        field.strTransactionAmount <= 0
      ) {
        swal(
          "Validation Error",
          "Please fill in all required fields (Account Type, Account Number, and Amount) in To Account Information.",
          "error"
        );
        return;
      }
    }

    if (!validateTransferAmount(mainTransferAmount, inputFields)) {
      return;
    }

    const totalTransferAmount = calculateTotalTransferAmount(inputFields);

    if (totalTransferAmount >= mainTransferAmount) {
      swal(
        "Validation Error",
        "Total transfer amount reached. Cannot add more rows.",
        "error"
      );
      setIsValidateButtonDisabled(true); // Disable the button
      return;
    }

    const newInputFields = [
      ...inputFields,
      {
        strToAccountType: "",
        strToAccountNo: "",
        strTransactionAmount: "",
        holdername: "",
        isRowValid: false, // Add a new property to track validation status
      },
    ];

    // Set the validation status of the previous row to true
    if (inputFields.length > 0) {
      const prevRowIdx = inputFields.length - 1;
      newInputFields[prevRowIdx].isRowValid = true;
    }
    setInputFields(newInputFields);
    setShowOldButton(true);
  };

  const handleInputChange = async (index, event) => {
    const values = [...inputFields];
    values[index][event.target.name] = event.target.value;

    const response = await amsApi.post(
      `account/getAccountBalancAndName`,
      {
        strAccountType: values[index].strToAccountType,
        strAccountNumber: values[index].strToAccountNo,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.data.code === "S0000") {
      values[index].holdername =
        response.data.accountInfoList[0].strAccountHolderName;
    } else {
      values[index].holdername = ""; // Clear holder name if fetching fails
    }

    setInputFields(values);
  };

  const [showOldButton, setShowOldButton] = useState(true);
  const handleOldButtonClick = () => {
    setIsRowAdded(false);
    setShowOldButton(false);
  };
  // end
  //Gl From Account holdernam
  const [data, setData] = useState([]);
  const AccountNumber = data.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("strAccountNumber", AccountNumber);
  let accountNumber = sessionStorage.getItem("strAccountNumber");

  const strGLAccountDescription = data.map((t) => [t.strGLAccountDescription]);
  sessionStorage.setItem("GLAccountDescription", strGLAccountDescription);
  let glAccountDescription = sessionStorage.getItem("GLAccountDescription");

  const ClosingBalance = data.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("strClosingBalance", ClosingBalance);
  let closingBalance = sessionStorage.getItem("strClosingBalance");
  useEffect(() => {
    Description();
    setError("");
  }, [accounttype]);
  const Description = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type/getGLDescrption`,
        {
          strGLAccountType: accounttype,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData(response.data.glAccountviewModels);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // Submit
  const GltoMAny = async () => {
    if (accounttype === "" || accounttype.length === 0 || amount === "  ") {
      setError(true);
    } else if (!useid) {
      swal("Please check your user id ");
    } else if (!accountNumber) {
      swal("Please check your From GL AccountNo ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/glAccToMany`,
          {
            strMakerId: useid,
            strFromGLAccountNo: accountNumber,
            strFromGLAccountType: accounttype,
            strTransferAmount: amount,
            bulkTransferToAccounts: inputFields,
            strIsVerified: "Y",
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
          clearFields();
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
  const clearFields = () => {
    setAccountType("");
    setAmount("");
    setData([]);
    setInputFields([
      {
        strToAccountType: "",
        strToAccountNo: "",
        strTransactionAmount: "",
        holdername: "",
      },
    ]);
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                GL To Many
              </p>
            </div>
          </div>
        </div>

        <div className="overflow-hidden shadow sm:rounded-md mt-2">
          <div className="grid  md:grid-cols-1 ">
            <div className="sm:p-2 bg-blue-100 ">
              <h1 className="font-medium flex justify-center  text-black text-2xl">
                From GL Account Information
              </h1>
              <div className="grid gap-6 md:grid-cols-3 lg:grid-cols-5 px-8 mt-1 ">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    GL Account Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="accountType"
                    name="accountType"
                    value={accounttype}
                    onChange={(e) => setAccountType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>

                    {type.map((data) => (
                      <optgroup key={uuidv4()}>
                        <option value={data.strGLAccountType}>
                          {data.strGLAccountType || ""} -
                          {data.strGLAccountDescription || ""}
                        </option>
                      </optgroup>
                    ))}
                  </select>
                  {error && accounttype.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Account Type !
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div className="">
                  {""}
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    // onClick={Description}
                    disabled
                    defaultValue={accountNumber}
                    autoComplete="off"
                    placeholder="Enter Gl Account number"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>

                <div className="">
                  {""}
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Description
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    disabled
                    autoComplete="off"
                    defaultValue={glAccountDescription}
                    placeholder="Enter GL Account Desc.."
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className="">
                  {""}
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Available Balance
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    disabled
                    autoComplete="off"
                    defaultValue={closingBalance}
                    placeholder="Enter Available Balance"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Transfer Amount
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
                        setError("Please Enter amount!");
                        setErrorMessage("");
                      }
                    }}
                    autoComplete="off"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Amount to Transfer"
                  />
                  {errorMessage ? (
                    <p className="text-red-500 text-xs font-medium">
                      {errorMessage}
                    </p>
                  ) : error && amount.length <= 0 ? (
                    <p className="text-red-500 text-xs font-medium">
                      Please Enter amount!
                    </p>
                  ) : null}
                </div>
              </div>
            </div>

            {/* adding input */}
            <div className="overflow-x-auto relative shadow-md bg-white">
              <h1 className="font-medium flex justify-center  text-black text-2xl ">
                To Account Information
              </h1>
              {inputFields.map((inputField, index) => (
                <div
                  key={index}
                  className="grid gap-3 mb-1 md:grid-cols-5 px-8 mt-3 "
                >
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      type="text"
                      placeholder="strToAccountType"
                      name="strToAccountType"
                      value={inputField.strToAccountType}
                      onChange={(event) => handleInputChange(index, event)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {totype.map((data) => (
                        <optgroup key={uuidv4()}>
                          <option value={data.strAccountType}>
                            {data.strAccountType || ""} -
                            {data.strDescription || ""}
                          </option>
                        </optgroup>
                      ))}
                    </select>
                  </div>
                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strToAccountNo"
                      name="strToAccountNo"
                      value={inputField.strToAccountNo}
                      onChange={(event) => handleInputChange(index, event)}
                      autoComplete="off"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account Number"
                    />
                  </div>

                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Holder name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="holdername"
                      name="holdername"
                      placeholder="Enter Account holder name"
                      value={inputField.holdername}
                      onChange={(event) => handleInputChange(index, event)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Amount To Transfer
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strTransactionAmount"
                      name="strTransactionAmount"
                      value={inputField.strTransactionAmount}
                      onChange={(event) => handleInputChange(index, event)}
                      autoComplete="off"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Amount To Transfer"
                    />
                  </div>

                  <div className="">
                    <label
                      htmlFor="text"
                      className="flex justify-start  text-xs font-medium text-gray-900 dark:text-white"
                    >
                      Action
                    </label>
                    {!isRowAdded || index !== inputFields.length - 1 ? (
                      <button
                        title="Add row Here"
                        onClick={handleOldButtonClick}
                        disabled={isRowAdded}
                        className="bg-blue-700 cursor-not-allowed block w-auto px-3 py-1 text-sm text-white bg-clip-padding  border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        Add Row{""}
                      </button>
                    ) : (
                      <button
                        title="Validate Data"
                        onClick={handleAddFields}
                        disabled={isValidateButtonDisabled}
                        className={`bg-red-700 block w-auto px-3 py-1 text-sm text-white bg-clip-padding border-solid border-gray-500 rounded transition ease-in-out m-0 ${
                          isValidateButtonDisabled ? "cursor-not-allowed" : ""
                        }`}
                      >
                        Validate
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
            <button
              title="Click Submit Button"
              type="button"
              onClick={GltoMAny}
              data-modal-toggle="defaultModal"
              className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
            >
              Submit
            </button>
            <button
              title="Clear Data"
              type="submit"
              data-modal-toggle="defaultModal"
              onClick={clearFields}
              className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
            >
              Clear
            </button>
          </div>
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
