import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CountTypeLinkedGl() {
  const [glaccounttype, setGlaccounttype] = useState("");
  const [data, setData] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [account, setAccount] = useState([]);
  const [currency, setCurrency] = useState("");
  const [multicurrency, setMulticurrency] = useState([]);
  const [error, setError] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const stripeAccountType = glaccounttype;
  const [glaccountType, glaccountNumber] = stripeAccountType.split("-");

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
    setCurrency("");
    setAccountType("");
    setGlaccounttype("");
    setError("");
  };

  useEffect(() => {
    accountytpe();
  }, []);

  const accountytpe = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getaccounttypelistbasedonmulticurrency`,
        {}
      );

      if (response.data.code === "S0000") {
        setAccount(response.data.accountDescription);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    Currency();
  }, [accounttype]);

  const Currency = async () => {
    if (accounttype === "" || accounttype.length === 0) {
      // setError("Data no");
    } else {
      try {
        const response = await amsApi.post(
          `multiCurrencyWallletAccounttype/getcurrencybasedonmulticurrency`,
          { strAccountType: accounttype },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data.code === "S0000") {
          setMulticurrency(response.data.multiCurrencyWalletAccountTypeMasters);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    }
  };

  useEffect(() => {
    gLAccountlist();
  }, []);

  const gLAccountlist = async () => {
    try {
      const response = await amsApi.get(`tcsTypeMaster/getGlType`, {});

      if (response.data.code === "S0000") {
        setData(response.data.listData);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (accounttype === "" || glaccounttype === "" || currency === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `multiCurrencyWallletAccounttype/updateCurrencyWalletLinkedGl`,
          {
            strAccountType: accounttype,
            currencyCode: currency,
            glAccountType: glaccountType,
            glAccountNumber: glaccountNumber,
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
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Multi Currency Account Type LinkedGl
              </p>
            </div>
          </div>
        </div>

        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4 py-2 sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-4">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-10 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Account Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      id="accounttype"
                      name="accounttype"
                      value={accounttype}
                      onChange={(e) => setAccountType(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {account.map((data) => (
                        <option className="capatlize text-sm">
                          {data.strAccountType}
                        </option>
                      ))}
                    </select>
                    {error && accounttype.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter Account Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-16 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Currency
                      <span className="text-red-600 px-0.5">*</span>
                    </label>
                    <select
                      id="Currency"
                      name="Currency"
                      disabled={!accounttype}
                      value={currency}
                      onChange={(e) => setCurrency(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {/* {multicurrency.map((currencyCode) => (
                        <option key={currencyCode} value={currencyCode}>
                          {currencyCode}
                        </option>
                      ))} */}
                      {multicurrency.map((item) => (
                        <option value={item.currencyCode}>
                          {item.currencyCode || ""}
                        </option>
                      ))}
                    </select>
                    {error && currency.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter currency code
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-6 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Gl Account Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      id="glaccounttype"
                      name="glaccounttype"
                      disabled={!currency}
                      value={glaccounttype}
                      onChange={(e) => setGlaccounttype(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {data.map((item) => (
                        <option value={item.strGLAccountType}>
                          {item.strGLAccountType || ""}
                        </option>
                      ))}
                    </select>
                    {error && glaccounttype.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter Gl account type
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
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
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
