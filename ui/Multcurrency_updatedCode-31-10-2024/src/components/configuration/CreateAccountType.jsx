import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateAccountType() {
  // let participantID = sessionStorage.getItem("Participantid");
  const participantID = sessionStorage.getItem("Participantid");
  const userId = localStorage.getItem("userName");
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
  const [typedata, setTypedata] = useState([]);
  let ntype = typedata.strNubanType;
  const [strAccountType, setstrAccountType] = useState("");
  const [strDescription, setstrDescription] = useState("");
  const [categoryList, setCategoryList] = useState("");
  const [alldatamap, setalldatamap] = useState([]);
  const [alldata1, setalldata1] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [taxType, settaxType] = useState("");
  const [strGLAccountType, setstrGLAccountType] = useState("");
  const [dormancyPeriodsInDays, setStrDormancyPeriodsInDays] = useState("");
  const [strStatus, setstrStatus] = useState("");
  const [strIsAllowWallet, setstrIsAllowWallet] = useState("");
  const [strIsAutomated, setstrIsAutomated] = useState("");
  const [strAccountTypeCode, setstrAccountTypeCode] = useState("");
  const [strTaxType, setstrTaxType] = useState("");
  const [strIGST, setstrIGST] = useState("");
  const [strVAT, setstrVAT] = useState("");
  const [strCGST, setstrCGST] = useState("");
  const [strSGST, setstrSGST] = useState("");
  const [catType, setCatType] = useState("");
  const [strAllowLoadCash, setstrAllowLoadCash] = useState("");
  const [strIsWithdrawAllowAtAgent, setstrIsWithdrawAllowAtAgent] =
    useState("");
  const [strIsDepositAllowAtAgent, setstrIsDepositAllowAtAgent] = useState("");
  const [strIsRevolvingCredit, setstrIsRevolvingCredit] = useState("");
  const [strIsCreditType, setstrIsCreditType] = useState("N");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const [errorMessage2, setErrorMessage2] = useState("");

  const [nType, setNType] = useState("");
  const [nType1, setNType1] = useState("");
  const NubanTypeHandler = (e) => {
    const nTypeData = e.target.value;
    if (nTypeData === "Y") {
      setNType(ntype);
      setNType1("Y");
    } else {
      setNType1("N");
      setNType("");
    }
  };
  const showComponentSGST = strTaxType === "SGST & CGST";
  const showComponentIGST = strTaxType === "IGST";
  const showComponentVAT = strTaxType === "VAT";
  // Account Type Category
  useEffect(() => {
    categoryListModelsList();
  }, []);
  const categoryListModelsList = async () => {
    try {
      const response = await amsApi.post(`category_type/categoryTypedata`, {
        strParticipantID: participantID,
      });

      if (response.data.code === "S0000") {
        setalldatamap(response.data.category);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // Trasaction Type Configuration
  useEffect(() => {
    categoryListModelsListByTaxConfig();
  }, [taxType]);
  const categoryListModelsListByTaxConfig = async () => {
    try {
      const response = await amsApi.post(
        `tax_type_config/getTaxtypeConfig`,
        {}
      );
      if (response.data.code === "S0000") {
        setalldata1(response.data.categoryListModelsListByTaxConfig);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // GL Account Creation List
  useEffect(() => {
    gLAccountCreationlist();
  }, []);
  const gLAccountCreationlist = async () => {
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

  // NUBAN Type
  useEffect(() => {
    nubantype();
  }, []);

  const nubantype = async () => {
    try {
      const response = await amsApi.post(
        `nubanCode/getNubanCode`,
        {
          strNubanType: "-",
          strParticipantId: participantID,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setTypedata(response.data.nubanCodeConfigObject);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // SUBMIT
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strAccountType === " " ||
      strAccountType.length === 0 ||
      strDescription === " " ||
      strAccountTypeCode === " " ||
      strGLAccountType === " "
    ) {
      setError(true);
    }
    try {
      const response1 = await amsApi.post(
        `accountType/saveAccountTypeInformation`,
        {
          strParticipantId: participantID,
          strAccountType: strAccountType,
          strDescription: strDescription,
          strNubanType: nType,
          strIsNubanconfigure: nType1,
          strAccNumStartDigit: strAccountTypeCode,
          strAccountTypeCode: strAccountTypeCode,
          strIsAutomated: strIsAutomated,
          strDormancyPeriodsInDays: dormancyPeriodsInDays,
          strStatus: strStatus,
          strIsAllowWallet: strIsAllowWallet,
          strCategoryType: categoryList,
          strGLAccountType: strGLAccountType,
          strAllowLoadCash: strAllowLoadCash,
          strIsRevolvingCredit: strIsRevolvingCredit,
          strIsWithdrawAllowAtAgent: strIsWithdrawAllowAtAgent,
          strIsDepositAllowAtAgent: strIsDepositAllowAtAgent,
          strSingleTxnLimit: "",
          strDailyTxnLimit: "",
          strMonthlyTxnLimit: "",
          strYearlyTxnLimit: "",
          strTaxType: strTaxType,
          strIGST: strIGST + "%",
          strVAT: strVAT + "%",
          strCGST: strCGST + "%",
          strSGST: strSGST + "%",
          strIsCreditType: strIsCreditType,
          strCreatedBy: userId,
        }
      );
      if (response1.data.code === "S0000") {
        //setalldata(response1.data.message);
        handleShowSuccess(response1.data.message);
        setError("");
        handleClick();
      } else {
        handleShowError(response1.data.message);
      }
    } catch (error) {
      handleShowError(error.response1.data.messageor);
    }
  };

  // clear input
  const handleClick = () => {
    setstrAccountType("");
    setstrDescription("");
    setstrStatus("");
    setstrIsAllowWallet("");
    setstrIsAutomated("");
    setstrAccountTypeCode("");

    strGLAccountType("");
    settaxType("");
    setCategoryList("");
    setstrIsCreditType("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full ">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Create Account Type
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 bg-white sm:rounded-md overflow-auto max-h-[27rem]">
          <div className="overflow-hidden shadow sm:rounded-md">
            <div className=" px-4 py-2 sm:p-1 bg-white  ">
              <div
                className={`${
                  nType1 === "Y"
                    ? "grid gap-3 mb-1 md:grid-cols-4  px-8"
                    : "grid gap-3 mb-1 md:grid-cols-3  px-8"
                }`}
              >
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Type<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strAccountType"
                    name="strAccountType"
                    value={strAccountType}
                    onChange={(e) =>
                      setstrAccountType(e.target.value.toUpperCase())
                    }
                    placeholder="Enter Account Type"
                    autoComplete="off"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                  {error && strAccountType.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Enter Account Type!
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
                    Description<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strDescription"
                    name="strDescription"
                    value={strDescription}
                    onChange={(e) => setstrDescription(e.target.value)}
                    autoComplete="off"
                    placeholder="Enter Description"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                  {error && strDescription.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Enter Description!
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
                    Is Nuban Account
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="nType1"
                    name="nType1"
                    value={nType1}
                    placeholder="Enter Account Generation Automate"
                    onChange={NubanTypeHandler}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value=""> Select</option>
                    <option value="Y"> Yes </option>
                    <option value="N"> No </option>
                  </select>
                </div>
                {nType1 === "Y" && (
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Nuban Type<span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      defaultValue={ntype}
                      disabled
                      autoComplete="off"
                      placeholder="Enter Nuban Type"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                )}
              </div>
              <div className="grid gap-4 mb-2 md:grid-cols-3  px-8">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Type Prefix Code
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strAccountTypeCode"
                    name="strAccountTypeCode"
                    maxLength={2}
                    minLength={2}
                    value={strAccountTypeCode}
                    // onChange={(e) => setstrAccountTypeCode(e.target.value)}
                    onChange={(e) => {
                      const value = e.target.value;
                      setstrAccountTypeCode(value);

                      const regex = /^[0-9]*$/;
                      if (!regex.test(value)) {
                        setErrorMessage(
                          " Only allow numeric digits"
                          // "Please enter only numeric Values"
                        );
                      } else {
                        setErrorMessage("");
                      }
                      setError("");
                    }}
                    onBlur={() => {
                      if (strAccountTypeCode.length === 0) {
                        setError("Please Enter Account Type Prefix Code!");
                        setErrorMessage("");
                      }
                    }}
                    autoComplete="off"
                    placeholder="Enter Account Type Prefix Code"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                  {errorMessage ? (
                    <p className="text-red-500 text-xs font-medium">
                      {errorMessage}
                    </p>
                  ) : error && strAccountTypeCode.length <= 0 ? (
                    <p className="text-red-500 text-xs font-medium">
                      Please Enter Account Type Prefix Code!
                    </p>
                  ) : null}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Generation Automate
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strIsAutomated"
                    name="strIsAutomated"
                    value={strIsAutomated}
                    placeholder="Enter Account Generation Automate"
                    onChange={(e) => setstrIsAutomated(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value=""> Select</option>
                    <option value="Y"> Yes </option>
                    <option value="N"> No </option>
                  </select>
                  {error && strIsAutomated.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Account Generation Automate
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
                    Dormant Period In Days
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strDormancyPeriodsInDays"
                    name="strDormancyPeriodsInDays"
                    value={dormancyPeriodsInDays}
                    // onChange={(e) =>
                    //   setStrDormancyPeriodsInDays(e.target.value)
                    // }
                    onChange={(e) => {
                      const value = e.target.value;
                      setStrDormancyPeriodsInDays(value);

                      const regex = /^[0-9]*$/;
                      if (!regex.test(value)) {
                        setErrorMessage1(
                          " Only allow numeric digits"
                          // "Please enter only numeric values"
                        );
                      } else {
                        setErrorMessage1("");
                      }
                      setError("");
                    }}
                    onBlur={() => {
                      if (dormancyPeriodsInDays.length === 0) {
                        setError(" Please Enter Dormant Period!");
                        setErrorMessage1("");
                      }
                    }}
                    autoComplete="off"
                    placeholder="Enter Dormant Period In Days"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                  {errorMessage1 ? (
                    <p className="text-red-500 text-xs font-medium">
                      {errorMessage1}
                    </p>
                  ) : error && dormancyPeriodsInDays.length <= 0 ? (
                    <p className="text-red-500 text-xs font-medium">
                      Please Enter Dormant Period!
                    </p>
                  ) : null}
                </div>
              </div>
              <div className="grid gap-4 mb-1 md:grid-cols-3  px-8">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Status
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strStatus"
                    name="strStatus"
                    value={strStatus}
                    onChange={(e) => setstrStatus(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value=""> Select</option>
                    <option value="Active">Active </option>
                    <option value="InActive">InActive </option>
                  </select>
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Allow Multi-Wallet{" "}
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strIsAllowWallet"
                    name="strIsAllowWallet"
                    value={strIsAllowWallet}
                    onChange={(e) => setstrIsAllowWallet(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value=""> Select </option>
                    <option value="Y">Yes</option>
                    <option value="N">No</option>
                  </select>
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Category List
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    name="categoryList"
                    id="categoryList"
                    value={categoryList}
                    onChange={(e) => {
                      for (let i = 0; i < alldatamap.length; i++) {
                        if (alldatamap[i].strType === e.target.value) {
                          setCatType(alldatamap[i].strCategoryType);
                          setstrIsCreditType(
                            alldatamap[i].strCategoryType === "C" ? "Y" : "N"
                          );
                        }
                      }
                      setCategoryList(e.target.value);
                    }}
                    placeholder="Enter Category List"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldatamap.map((data) => (
                      <option
                        className="capatlize text-sm"
                        value={data.strType}
                      >
                        {data.strType || "-"} - {data.strDescription || "-"}
                      </option>
                    ))}
                  </select>
                  {error && categoryList.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Category Type!
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
                    GL Account Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    name="strGLAccountType"
                    id="strGLAccountType"
                    value={strGLAccountType}
                    onChange={(e) => setstrGLAccountType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldata2.map((data) => (
                      <option className="capatlize text-sm">
                        {data.strGLAccountType}-{data.strGLAccountNumber}
                      </option>
                    ))}
                  </select>
                  {error && strGLAccountType.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select GL Account Type!
                    </p>
                  ) : (
                    ""
                  )}
                </div>
              </div>
              {categoryList !== "C" && categoryList !== "" && (
                <div className="grid gap-4 mb-2 md:grid-cols-3  px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Allow Load Balance Count In a Month
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="number"
                      id="strAllowLoadCash"
                      name="strAllowLoadCash"
                      value={strAllowLoadCash}
                      onChange={(e) => {
                        const value = e.target.value;
                        setstrAllowLoadCash(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage2(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage2("");
                        }
                        // setError("");
                      }}
                      autoComplete="off"
                      placeholder="Enter Allow Load Balance In a Month"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {errorMessage2 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage2}
                      </p>
                    ) : null}
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Withdraw Allow At Agent{" "}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strIsWithdrawAllowAtAgent"
                      name="strIsWithdrawAllowAtAgent"
                      value={strIsWithdrawAllowAtAgent}
                      onChange={(e) =>
                        setstrIsWithdrawAllowAtAgent(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="N">NO</option>
                      <option value="Y">YES</option>
                    </select>
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Deposit Allow At agent
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strIsDepositAllowAtAgent"
                      name="strIsDepositAllowAtAgent"
                      value={strIsDepositAllowAtAgent}
                      onChange={(e) =>
                        setstrIsDepositAllowAtAgent(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="N">NO</option>
                      <option value="Y">YES</option>
                    </select>
                  </div>
                </div>
              )}
              {categoryList === "C" && (
                <div className="grid gap-4 mb-2 md:grid-cols-3  px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Credit Card Config{" "}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strIsRevolvingCredit"
                      name="strIsRevolvingCredit"
                      value={strIsRevolvingCredit}
                      onChange={(e) => setstrIsRevolvingCredit(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select Credit Card Config </option>
                      <option value="Y">Revolving For Credit</option>
                      <option value="N">Block For MCC</option>
                    </select>
                  </div>
                </div>
              )}

              <div>
                <h2 className="font-normal md:font-semibold px-8 ">Tax Type</h2>
              </div>
              <div className="grid gap-3 mb-1 md:grid-cols-3  px-8">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Tax Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    name="strTaxType"
                    id="strTaxType"
                    value={strTaxType}
                    onChange={(e) => setstrTaxType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldata1.map((data) => (
                      <option
                        className="capatlize text-sm"
                        value={data.strTaxType}
                      >
                        {data.strTaxType}
                      </option>
                    ))}
                  </select>
                  {error && strTaxType.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Tax Type!
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                {showComponentIGST && (
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      IGST <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strIGST"
                      name="strIGST"
                      value={strIGST + "%"}
                      onChange={(e) => {
                        const inputValue = e.target.value.replace("%", "");
                        if (
                          inputValue === "" ||
                          (parseFloat(inputValue) >= 0 &&
                            parseFloat(inputValue) <= 100)
                        ) {
                          setstrIGST(inputValue);
                        }
                      }}
                      placeholder="Enter IGST"
                      // autoComplete="off"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                )}
                {showComponentVAT && (
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      VAT <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strVAT"
                      name="strVAT"
                      value={strVAT + "%"}
                      onChange={(e) => {
                        const inputValue = e.target.value.replace("%", "");
                        if (
                          inputValue === "" ||
                          (parseFloat(inputValue) >= 0 &&
                            parseFloat(inputValue) <= 100)
                        ) {
                          setstrVAT(inputValue);
                        }
                      }}
                      autoComplete="off"
                      placeholder="Enter VAT"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                )}
              </div>

              {/* showComponentSGST */}
              {showComponentSGST && (
                <div className="grid gap-3 mb-1 md:grid-cols-2  px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      CGST <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strCGST"
                      name="strCGST"
                      value={strCGST + "%"}
                      onChange={(e) => {
                        const inputValue = e.target.value.replace("%", "");
                        if (
                          inputValue === "" ||
                          (parseFloat(inputValue) >= 0 &&
                            parseFloat(inputValue) <= 50)
                        ) {
                          setstrCGST(inputValue);
                        }
                      }}
                      autoComplete="off"
                      placeholder="Enter CGST"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      SGST <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strSGST"
                      name="strSGST"
                      value={strCGST + "%"}
                      // onChange={(e) => {
                      //   const inputValue = e.target.value.replace("%", "");
                      //   if (
                      //     inputValue === "" ||
                      //     (parseFloat(inputValue) >= 0 &&
                      //       parseFloat(inputValue) <= 50)
                      //   ) {
                      //     setstrSGST(inputValue);
                      //   }
                      // }}
                      // autoComplete="off"
                      placeholder="Enter SGST"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                  </div>
                </div>
              )}
            </div>
          </div>
          {/* </form> */}
        </div>
        <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
          <button
            type="button"
            data-modal-toggle="defaultModal"
            onClick={handleSubmit}
            className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
          >
            Submit
          </button>
          <button
            type="button"
            onClick={handleClick}
            className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
          >
            Clear
          </button>
        </div>
      </div>
      {/* </div> */}

      {/* Delete model */}

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
