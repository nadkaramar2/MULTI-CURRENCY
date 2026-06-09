import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";

// import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateCurrency() {
  const [alldataCountry, setalldataCountry] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [alldata, setAlldata] = useState([]);
  const [country, setcountry] = useState("");
  const [currencyCode, setcurrencyCode] = useState("");
  const [currencySymbol, setcurrencySymbol] = useState("");
  const [currencyDescr, setcurrencyDescr] = useState("");
  const [baseCountry, setbaseCountry] = useState("");
  const [feetype, setFeetype] = useState("");
  const [tcstype, setTcstype] = useState("");
  const [chargeType, setChargetype] = useState("");
  const [networkType, setNetworktype] = useState("");

  const [status, setstatus] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [glAccountType, setglAccountType] = useState("");
  const userId = localStorage.getItem("userName");
  //const [glAccountNumber, setglAccountNumber] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleClick = () => {
    setcountry("");
    setcurrencyCode("");
    setcurrencyDescr("");
    setstatus("");
    setbaseCountry("");
    setglAccountType("");
    // setNetworktype("");
    // setChargetype("");
    setTcstype("");
    setFeetype("");
  };

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

  // COUNTRY
  useEffect(() => {
    countryName();
  }, [country]);

  const countryName = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {});

      if (response.data.code === "S0000") {
        setalldataCountry(response.data.countryList);
      } else {
      }
    } catch (error) {}
  };
  // GL Account Creation List
  useEffect(() => {
    gLAccountlist();
  }, []);

  const gLAccountlist = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/getGlAccTypeAccNumbr`,
        {}
      );

      if (response.data.code === "S0000") {
        setalldata2(response.data.gLAccountCreationlist);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    gLAccountCreationlist();
  }, [setcountry]);

  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACTypeListByParticiptWise`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTypeMasterlistData);
      } else {
        // showSuccess(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // fee type
  const [fee, setFee] = useState([]);
  useEffect(() => {
    Feetype();
  }, []);

  const Feetype = async () => {
    try {
      const response = await amsApi.get(`feeTypeMaster/getFeeAccountType`, {});

      if (response.data.code === "S0000") {
        setFee(response.data.feeTypeList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // Tcs type
  const [tcs, setTcs] = useState([]);
  useEffect(() => {
    TCStype();
  }, []);
  const TCStype = async () => {
    try {
      const response = await amsApi.get(`tcsTypeMaster/getTcsType`, {});
      if (response.data.code === "S0000") {
        setTcs(response.data.tcsTypeList);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  // Charge Type

  // const [charge, setCharge] = useState([]);
  // useEffect(() => {
  //   Chargetype();
  // }, []);

  // const Chargetype = async () => {
  //   try {
  //     const response = await amsApi.post(`chargetype/getchargetype`, {});

  //     if (response.data.code === "S0000") {
  //       setCharge(response.data.chargeTypeList);
  //     } else {
  //       // handleShowError(response.data.message);
  //     }
  //   } catch (error) {
  //     // handleShowError(error.response.data.message);
  //   }
  // };

  // network type
  // const [network, setNetwork] = useState([]);
  // useEffect(() => {
  //   getnetwok();
  // }, []);

  // const getnetwok = async () => {
  //   try {
  //     const response = await amsApi.get(`networktype/getNetworkTypelist`, {});

  //     if (response.data.code === "S0000") {
  //       setNetwork(response.data.networkTypeList);
  //     } else {
  //       handleShowError(response.data.message);
  //     }
  //   } catch (error) {
  //     handleShowError(error.response.data.message);
  //   }
  // };
  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      country === "" ||
      country.length === 0 ||
      currencyCode === "" ||
      currencyCode.length === 0 ||
      currencyDescr === "" ||
      baseCountry === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`currency-master/addCurrency`, {
          country: country,
          currencyCode: currencyCode,
          currencySymbol: currencySymbol,
          currencyDescr: currencyDescr,
          baseCountry: baseCountry,
          status: status,
          glAccountType: glAccountType.split("-")[0],
          glAccountNumber: glAccountType.split("-")[1],
          createdBy: userId,
          feeType: feetype,
          tcsType: tcstype,
          chargeType,
          networkType,
        });

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
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
                Add Currency
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-2 sm:p-2 bg-white  ">
                <div className="grid gap-2 mb-2 px-4 md:grid-cols-4 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Country
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      name="country"
                      id="country"
                      value={country}
                      onChange={(e) => setcountry(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldataCountry.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.countryId}
                        >
                          {data.countryName}
                        </option>
                      ))}
                    </select>
                    {error && country.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Country
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Currency Code
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      maxLength={4}
                      id="currencyCode"
                      disabled={!country}
                      value={currencyCode}
                      onChange={(e) => {
                        const value = e.target.value.toUpperCase();
                        setcurrencyCode(value);
                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage("Input can't be numeric");
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (currencyCode.length === 0) {
                          setError("Please Enter Currency Code");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Currency Code"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && currencyCode.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Currency Code
                      </p>
                    ) : null}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Currency Symbol
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      minLength={1}
                      maxLength={3}
                      type="text"
                      id="currencySymbol"
                      disabled={!currencyCode}
                      value={currencySymbol}
                      onChange={(e) => setcurrencySymbol(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Currency Symbol"
                      autoComplete="off"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Currency Description
                      <span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="currencyDescr"
                      disabled={!currencySymbol}
                      value={currencyDescr}
                      onChange={(e) => setcurrencyDescr(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Currency Descr"
                      autoComplete="off"
                    />
                    {error && currencyDescr.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Currency Desc
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Base Country
                      <span className="text-red-600">*</span>
                    </label>
                    <select
                      id="baseCountry"
                      name="baseCountry"
                      disabled={!currencyDescr}
                      value={baseCountry}
                      onChange={(e) => setbaseCountry(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Y"> Yes</option>
                      <option value="N">No</option>
                    </select>
                    {error && baseCountry.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Base Country
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {baseCountry === "Y" && (
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Tcs Type
                        <span className="text-red-600">*</span>
                      </label>
                      <select
                        name="tcstype"
                        id="tcstype"
                        value={tcstype}
                        onChange={(e) => setTcstype(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {tcs.map((tcsType) => (
                          <option key={tcsType} value={tcsType}>
                            {tcsType}
                          </option>
                        ))}
                      </select>
                      {error && tcstype.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Tcs Type!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  )}
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Status<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="status"
                      name="status"
                      disabled={!baseCountry}
                      value={status}
                      onChange={(e) => setstatus(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Active"> Active </option>
                      <option value="InActive">InActive</option>
                    </select>
                    {error && status.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Status
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      GL Account Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      name="glAccountType"
                      id="glAccountType"
                      disabled={!status}
                      value={glAccountType}
                      onChange={(e) => setglAccountType(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata2.map((data) => (
                        <option className="capatlize text-sm">
                          {data.strGLAccountType}-{data.strGLAccountNumber}
                        </option>
                      ))}
                    </select>
                    {error && glAccountType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select GL Account Type!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {glAccountType !== "" && (
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        GL Account Number
                        <span className="text-red-600 ">*</span>
                      </label>

                      <input
                        name="glAccountNumber"
                        id="glAccountNumber"
                        value={glAccountType.split("-")[1]}
                        // onChange={(e) => setglAccountNumber(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                    </div>
                  )}
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Fee Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      name="feetype"
                      id="feetype"
                      disabled={!glAccountType}
                      value={feetype}
                      onChange={(e) => setFeetype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {fee.map((feeType) => (
                        <option key={feeType} value={feeType}>
                          {feeType}
                        </option>
                      ))}
                    </select>
                    {error && feetype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select fee Type!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  {/* 
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Charge Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      name="chargetype"
                      id="chargetype"
                      disabled={!tcstype}
                      value={chargetype}
                      onChange={(e) => setChargetype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {charge.map((chargeType) => (
                        <option key={chargeType} value={chargeType}>
                          {chargeType}
                        </option>
                      ))}
                    </select>
                    {error && chargetype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Charge Type!
                      </p>
                    ) : (
                      ""
                    )}
                  </div> */}
                  {/* <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Network Type
                      <span className="text-red-600 ">*</span>
                    </label>
                    <select
                      name="networktype"
                      id="networktype"
                      disabled={!chargetype}
                      value={networktype}
                      onChange={(e) => setNetworktype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {network.map((networkType) => (
                        <option key={networkType} value={networkType}>
                          {networkType}
                        </option>
                      ))}
                    </select>
                    {error && chargetype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select charge Type!
                      </p>
                    ) : (
                      ""
                    )}
                  </div> */}
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
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
