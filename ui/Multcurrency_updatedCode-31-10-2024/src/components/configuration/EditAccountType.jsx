import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useParams, useNavigate } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";

export default function EditAccountType() {
  const navigate = useNavigate();
  const { strAccountType } = useParams();
  const [alldatamap, setalldatamap] = useState([]);
  const [alldata1, setalldata1] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [alldatatype, setalldatatype] = useState([]);
  const [strParticipantId, setstrParticipantId] = useState("");
  const [taxType, settaxType] = useState("");
  const [glAccountType, setglAccountType] = useState("");
  const [strIsAllowWallet, setstrIsAllowWallet] = useState("");
  const [strIsDepositAllowAtAgent, setstrIsDepositAllowAtAgent] = useState("");
  const [strIsWithdrawAllowAtAgent, setstrIsWithdrawAllowAtAgent] =
    useState("");
  const [strCumulativeBalanceLimit3, setstrCumulativeBalanceLimit3] =
    useState("");
  const [strIGST, setstrIGST] = useState("");
  let participantID = sessionStorage.getItem("Participantid");

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
    categoryListModelsList();
  }, [strParticipantId]);

  const categoryListModelsList = async () => {
    try {
      const response = await amsApi.post(
        `account_type_category/getAccounttypeCategory`,
        {
          strParticipantId: participantID,
        }
      );

      if (response.data.code === "S0000") {
        setalldatamap(response.data.categoryListModelsList);
        // showSuccess(response.data.message);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
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
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    gLAccountCreationlist();
  }, [strParticipantId]);

  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/getGlAccTypeAccNumbr`,
        {}
      );

      if (response.data.code === "S0000") {
        setalldata2(response.data.gLAccountCreationlist);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    handleSubmitView();
  }, []);

  const handleSubmitView = async (event) => {
    try {
      const response = await amsApi.post(
        `accountType/getAccountTypeMasterDetailsBasedonAccountType`,
        {
          strAccountType: strAccountType,
        }
      );
      if (response.data.code === "S0000") {
        setalldatatype(response.data.accountTypeMaster2);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  const handleClick = () => {
    setstrParticipantId("");
    setglAccountType("");
    setstrIsAllowWallet("");
    setstrIsDepositAllowAtAgent("");
    setstrIsWithdrawAllowAtAgent("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
                Edit account Type
              </p>
            </div>
          </div>
        </div>
        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[32rem]">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-2 sm:p-2 bg-white  ">
                {alldatatype.map((data) => (
                  <div>
                    <div className="grid gap-4 mb-3 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Type<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strAccountType}
                          // disabled
                          // value={data.strAccountType}

                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          placeholder="Enter Account Type"
                          autoComplete="off"
                          className="block w-full px-1 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Description<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDescription}
                          defaultValue={data.strDescription}
                          // onChange={(e) => setstrDescription(e.target.value)}
                          id="strDescription"
                          autoComplete="off"
                          placeholder="Enter Description"
                          // placeholder="Enter your Current Pin"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Number Starting Digit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccNumStartDigit}
                          defaultValue={data.strAccNumStartDigit}
                          // onChange={(e) => setstrAccNumStartDigit(e.target.value)}
                          id="strAccNumStartDigit"
                          autoComplete="off"
                          placeholder="Enter AccNumStartDigit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Dormant Period In Days
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          // onChange={(e) =>
                          //   setstrDormancyPeriodsInDays(e.target.value)
                          // }
                          defaultValue={data.strDormancyPeriodsInDays}
                          id="strDormancyPeriodsInDays"
                          placeholder="Enter DormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Status
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strStatus"
                          name="strStatus"
                          defaultValue={data.strStatus}
                          // value={strStatus}
                          // onChange={(e) => setstrStatus(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="Active">Active </option>
                          <option value="InActive">InActive </option>
                          {/* <option className="captlize text-lg"></option> */}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Allow Multi-Wallet
                          <span className="text-red-600">*</span>
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
                          Category List<span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strParticipantId"
                          id="strParticipantId"
                          value={strParticipantId}
                          onChange={(e) => setstrParticipantId(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldatamap.map((data) => (
                            <option
                              className="capatlize text-sm"
                              // value={}
                            >
                              {data.strType || "-"} -{""}
                              {data.strDescription || "-"}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          GL Account Type
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="glAccountType"
                          id="glAccountType"
                          value={glAccountType}
                          onChange={(e) => setglAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata2.map((data) => (
                            <option
                              className="capatlize text-xs"
                              // value={}
                            >
                              {data.strGLAccountType} -{""}
                              {data.strGLAccountNumber || "-"}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Allow Load Balance Count In A Month
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strAllowLoadCash}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Credit Card Config
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strIsAllowWallet"
                          name="strIsAllowWallet"
                          value={strIsAllowWallet}
                          onChange={(e) => setstrIsAllowWallet(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option> Select Credit Card Config </option>
                          <option value="Y">Revolving For Credit</option>
                          <option value="N">Block For MCC</option>
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Withdraw Allow At Agent
                          <span className="text-red-600">*</span>
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
                          <option value="Y">Yes</option>
                          <option value="N">No</option>
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Deposit Allow At Agent
                          <span className="text-red-600">*</span>
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
                          <option value="Y">Yes</option>
                          <option value="N">No</option>
                        </select>
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-2">
                        Transaction Limit Details
                      </h2>
                    </div>
                    <div className="grid gap-4 mb-3 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Per Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          placeholder="Enter Per Transaction Limit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Daily Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          placeholder="Enter Enter Daily Transaction Limit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Monthly Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          placeholder="Enter Monthly Transaction Limit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Yearly Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          placeholder="Enter Yearly Transaction Limit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-2">
                        Tier1 Limit Details
                      </h2>
                    </div>
                    <div className="grid gap-4 mb-3 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Daily comulative Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strDailyCumulativeTxnLimit1}
                          // value={strDailyCumulativeTxnLimit1}
                          // onChange={(e) =>
                          //   setstrDailyCumulativeTxnLimit1(e.target.value)
                          // }
                          id="strDailyCumulativeTxnLimit1"
                          autoComplete="off"
                          placeholder="Enter Daily comulative Transaction Limit"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Comulative Balance Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strCumulativeBalanceLimit1}
                          // value={strCumulativeBalanceLimit1}
                          // onChange={(e) =>
                          //   setstrCumulativeBalanceLimit1(e.target.value)
                          // }
                          id="strCumulativeBalanceLimit1"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-2">
                        Tier2 Limit Details
                      </h2>
                    </div>

                    <div className="grid gap-4 mb-3 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Daily comulative Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strDailyCumulativeTxnLimit2}
                          // value={strDailyCumulativeTxnLimit2}
                          // onChange={(e) =>
                          //   setstrDailyCumulativeTxnLimit2(e.target.value)
                          // }
                          id="strDailyCumulativeTxnLimit2"
                          autoComplete="off"
                          // placeholder="Enter your Current Pin"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Comulative Balance Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strCumulativeBalanceLimit2}
                          // onChange={(e) =>
                          //   setstrCumulativeBalanceLimit2(e.target.value)
                          // }
                          defaultValue={data.strCumulativeBalanceLimit2}
                          // id="strCumulativeBalanceLimit2"
                          autoComplete="off"
                          // placeholder="Enter your Current Pin"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold py-2">
                        Tier3 Limit Details
                      </h2>
                    </div>
                    <div className="grid gap-6 mb-6 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Daily comulative Transaction Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDailyCumulativeTxnLimit3}
                          defaultValue={data.strDailyCumulativeTxnLimit3}
                          // onChange={(e) =>
                          //   setstrDailyCumulativeTxnLimit3(e.target.value)
                          // }
                          id="strDailyCumulativeTxnLimit3"
                          autoComplete="off"
                          // placeholder="Enter your Current Pin"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Comulative Balance Limit
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strCumulativeBalanceLimit3}
                          defaultValue={data.strDailyCumulativeTxnLimit3}
                          // onChange={(e) =>
                          //   setstrCumulativeBalanceLimit3(e.target.value)
                          // }
                          id="strCumulativeBalanceLimit3"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <h2 className="font-xm md:font-bold">Tax Type</h2>
                      </div>
                    </div>
                    <div className="grid gap-4 mb-3 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Tax Type<span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strTaxType"
                          id="strTaxType"
                          defaultValue={data.strTaxType}
                          // value={strTaxType}
                          // onChange={(e) => setstrTaxType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata1.map((data) => (
                            <option
                              className="capatlize text-sm"
                              // value={}
                            >
                              {data.strTaxType}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          IGST<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          value={strIGST}
                          onChange={(e) => setstrIGST(e.target.value)}
                          id="strIGST"
                          autoComplete="off"
                          placeholder="Enter your IGST"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          VAT<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strCumulativeBalanceLimit3}
                          onChange={(e) =>
                            setstrCumulativeBalanceLimit3(e.target.value)
                          }
                          id="vat"
                          autoComplete="off"
                          placeholder="Enter your VAT"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          CGST<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strCumulativeBalanceLimit3}
                          // onChange={(e) =>
                          //   setstrCumulativeBalanceLimit3(e.target.value)
                          // }
                          id="cgst"
                          autoComplete="off"
                          placeholder="Enter your CGST"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          SGST<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strCumulativeBalanceLimit3}
                          // onChange={(e) =>
                          //   setstrCumulativeBalanceLimit3(e.target.value)
                          // }
                          // id="price"
                          autoComplete="off"
                          placeholder="Enter your SGST"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="bg-gray-100 px-4 py-3 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-2 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent  bg-rose-500 py-1.5 px-3 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
                <button
                  title="Go Back"
                  type="button"
                  onClick={() => navigate(-1)}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-2 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  BACK
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
      {/* </div> */}
    </AppLayout>
  );
}
